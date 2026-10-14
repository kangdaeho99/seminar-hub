package com.seminarhub.api.settlement.usecase;

import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.ORDERED;
import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.PAID;
import static com.seminarhub.statemachine.result.TransitionRejectionCode.PAYMENT_NOT_CONFIRMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.seminarhub.api.settlement.flow.MemberSeminarItemFlow;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.repository.MemberRepository;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.enums.PaymentStatus;
import com.seminarhub.domain.payment.repository.PaymentRepository;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.domain.MemberSeminarItemStatusHistory;
import com.seminarhub.domain.seminar.domain.Seminar;
import com.seminarhub.domain.seminar.repository.MemberSeminarItemRepository;
import com.seminarhub.domain.seminar.repository.MemberSeminarRepository;
import com.seminarhub.domain.seminar.repository.SeminarRepository;
import com.seminarhub.domain.seminar.service.MemberSeminarItemStatusHistoryService;
import com.seminarhub.statemachine.exception.PaymentNotConfirmedException;
import com.seminarhub.statemachine.result.ItemTransitionResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.squirrelframework.foundation.exception.SquirrelRuntimeException;
import org.squirrelframework.foundation.exception.TransitionException;

/**
 * Real Spring proxies, Squirrel transitions and H2 persistence; no test-managed transaction.
 * The production guard throws the exception; only the upper batch caller is test-specific.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "spring.datasource.url=jdbc:h2:mem:guard-transaction;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@ActiveProfiles("test")
@Import(GuardExceptionTransactionIntegrationTest.LabConfiguration.class)
class GuardExceptionTransactionIntegrationTest {

    @Autowired private MemberSeminarItemUseCase useCase;
    @Autowired private BatchCaller batchCaller;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberSeminarRepository orderRepository;
    @Autowired private SeminarRepository seminarRepository;
    @Autowired private MemberSeminarItemRepository itemRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private MemberSeminarItemStatusHistoryService historyService;
    @Autowired private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactions;
    private Long orderId;
    private List<Long> itemIds;

    @BeforeEach
    void createOneHundredOrderedItemsWithOnlyFiveSuccessfulPayments() {
        transactions = new TransactionTemplate(transactionManager);
        // Commit the fixture before the operation under test starts.
        itemIds = transactions.execute(status -> {
            Member member = memberRepository.save(Member.builder().build());
            orderId = orderRepository.save(MemberSeminar.builder().member(member).build()).getId();
            Long seminarId = seminarRepository.save(Seminar.builder()
                    .name("Guard transaction lab " + UUID.randomUUID()).build()).getId();
            List<Long> ids = new ArrayList<>();
            for (int index = 0; index < 100; index++) {
                MemberSeminarItem item = useCase.create(orderId, seminarId);
                ids.add(item.getId());
                if (index < 5) {
                    paymentRepository.save(Payment.builder()
                            .memberSeminarItem(item)
                            .paymentAmount(BigDecimal.TEN)
                            .paymentStatus(PaymentStatus.SUCCESS)
                            .build());
                }
            }
            return List.copyOf(ids);
        });
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }

    @Test
    @DisplayName("Guard throw: item 6 aborts the shared transaction and rolls back the first 5")
    void uncaughtGuardExceptionRollsBackEarlierSuccessesAndStopsTheLoop() {
        TransitionException exception = assertThrows(TransitionException.class,
                () -> batchCaller.confirmAll(orderId, itemIds));

        PaymentNotConfirmedException rejection = assertInstanceOf(PaymentNotConfirmedException.class, rootCause(exception));
        assertEquals(itemIds.get(5), rejection.getItemId());
        assertEquals(PAYMENT_NOT_CONFIRMED, rejection.getCode());
        assertEquals(6, batchCaller.getAttemptedCount()); // Items 7..100 were never attempted.
        assertPersistedStatusesAndHistory(0);
    }

    @Test
    @DisplayName("Catch outside REQUIRED use case: all 100 attempted, but commit throws UnexpectedRollbackException")
    void catchingAfterTheInnerProxyCannotUndoRollbackOnly() {
        assertThrows(UnexpectedRollbackException.class,
                () -> batchCaller.confirmAllCatchingGuardFailures(orderId, itemIds));

        assertEquals(100, batchCaller.getAttemptedCount());
        assertPersistedStatusesAndHistory(0);
    }

    @Test
    @DisplayName("No outer transaction: 95 guard failures roll back independently and 5 successes commit")
    void catchingOutsideEachIndependentTransactionPreservesFiveSuccesses() {
        // Failures first, so this also proves the batch continues to later successful items.
        List<ItemTransitionResult> results = batchCaller.confirmIndependently(orderId, itemIds.reversed());

        assertFiveSuccessesAndNinetyFiveRejections(results);
        assertEquals(itemIds.reversed(), results.stream().map(ItemTransitionResult::getItemId).toList());
        assertEquals(100, batchCaller.getAttemptedCount());
        assertPersistedStatusesAndHistory(5);
    }

    private void assertFiveSuccessesAndNinetyFiveRejections(List<ItemTransitionResult> results) {
        assertEquals(100, results.size());
        assertEquals(5, results.stream().filter(ItemTransitionResult::isSuccess).count());
        List<ItemTransitionResult> rejected = results.stream().filter(result -> !result.isSuccess()).toList();
        assertEquals(95, rejected.size());
        rejected.forEach(result -> {
            assertEquals(PAYMENT_NOT_CONFIRMED, result.getCode());
            assertEquals(PAYMENT_NOT_CONFIRMED.getMessage(), result.getMessage());
        });
    }

    private void assertPersistedStatusesAndHistory(int paidCount) {
        // A fresh transaction reloads the DB, rather than inspecting mutated/detached Java objects.
        transactions.executeWithoutResult(status -> {
            assertEquals(100, itemRepository.findAllById(itemIds).size());
            for (int index = 0; index < itemIds.size(); index++) {
                Long itemId = itemIds.get(index);
                boolean paid = index < paidCount;
                assertEquals(paid ? PAID : ORDERED,
                        itemRepository.findById(itemId).orElseThrow().getMemberSeminarItemStatus(),
                        "Persisted status for item " + itemId);
                assertEquals(paid ? List.of(ORDERED, PAID) : List.of(ORDERED),
                        historyService.findByMemberSeminarItemId(itemId).stream()
                                .map(MemberSeminarItemStatusHistory::getToStatus).toList(),
                        "Persisted history for item " + itemId);
            }
        });
    }

    private static Throwable rootCause(Throwable exception) {
        Throwable cause = exception;
        while (true) {
            // Squirrel 0.3.10 stores its original exception separately from Throwable.cause.
            Throwable next = cause instanceof SquirrelRuntimeException squirrel
                    ? squirrel.getTargetException() : cause.getCause();
            if (next == null || next == cause) {
                return cause;
            }
            cause = next;
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class LabConfiguration {
        @Bean
        BatchCaller batchCaller(MemberSeminarItemFlow flow) {
            return new BatchCaller(flow);
        }
    }

    /** Test-only upper caller; all item operations go through the actual Flow and proxied UseCase. */
    static class BatchCaller {
        private final MemberSeminarItemFlow flow;
        private int attemptedCount;

        BatchCaller(MemberSeminarItemFlow flow) {
            this.flow = flow;
        }

        public int getAttemptedCount() {
            return attemptedCount;
        }

        @Transactional
        public List<ItemTransitionResult> confirmAll(Long orderId, List<Long> itemIds) {
            return process(orderId, itemIds, false);
        }

        @Transactional
        public List<ItemTransitionResult> confirmAllCatchingGuardFailures(Long orderId, List<Long> itemIds) {
            return process(orderId, itemIds, true);
        }

        // With no outer transaction, the existing REQUIRED use case starts a new one per call.
        public List<ItemTransitionResult> confirmIndependently(Long orderId, List<Long> itemIds) {
            assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
            return process(orderId, itemIds, true);
        }

        private List<ItemTransitionResult> process(Long orderId, List<Long> itemIds, boolean catchGuardFailures) {
            attemptedCount = 0;
            List<ItemTransitionResult> results = new ArrayList<>();
            for (Long itemId : itemIds) {
                attemptedCount++;
                try {
                    results.add(flow.confirmPayment(orderId, itemId));
                } catch (RuntimeException exception) {
                    // Squirrel may wrap the original failure. Never convert unrelated infrastructure errors.
                    if (!catchGuardFailures || !(rootCause(exception) instanceof PaymentNotConfirmedException rejection)) {
                        throw exception;
                    }
                    assertEquals(itemId, rejection.getItemId());
                    results.add(new ItemTransitionResult(itemId, rejection.getCode()));
                }
            }
            return results;
        }
    }
}
