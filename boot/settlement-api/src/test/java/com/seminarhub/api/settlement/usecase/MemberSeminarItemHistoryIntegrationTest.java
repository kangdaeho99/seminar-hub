package com.seminarhub.api.settlement.usecase;

import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.ORDERED;
import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.PAID;
import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.CANCELLED;
import static com.seminarhub.domain.seminar.enums.MemberSeminarStatus.FULL_CANCELED;
import static com.seminarhub.domain.seminar.enums.MemberSeminarStatus.PARTIAL_CANCELLED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.squirrelframework.foundation.exception.TransitionException;

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
import com.seminarhub.domain.seminar.repository.MemberSeminarItemStatusHistoryRepository;
import com.seminarhub.domain.seminar.repository.MemberSeminarRepository;
import com.seminarhub.domain.seminar.repository.SeminarRepository;
import com.seminarhub.domain.seminar.service.MemberSeminarItemStatusHistoryService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "spring.datasource.url=jdbc:h2:mem:item-history;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@ActiveProfiles("test")
class MemberSeminarItemHistoryIntegrationTest {

    @Autowired
    private MemberSeminarItemUseCase useCase;
    @Autowired
    private MemberSeminarItemStatusHistoryService historyService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberSeminarRepository orderRepository;
    @Autowired
    private SeminarRepository seminarRepository;
    @Autowired
    private MemberSeminarItemRepository itemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @MockitoSpyBean
    private MemberSeminarItemStatusHistoryRepository historyRepository;

    private TransactionTemplate transactions;
    private Long orderId;
    private Long seminarId;

    @BeforeEach
    void setUp() {
        transactions = new TransactionTemplate(transactionManager);
        transactions.executeWithoutResult(status -> {
            Member member = memberRepository.save(Member.builder().build());
            orderId = orderRepository.save(MemberSeminar.builder().member(member).build()).getId();
            seminarId = seminarRepository.save(Seminar.builder()
                    .name("History test " + UUID.randomUUID()).build()).getId();
        });
    }

    @Test
    void commitsTheCreatedItemAndItsInitialOrderedHistory() {
        MemberSeminarItem item = useCase.create(orderId, seminarId);

        transactions.executeWithoutResult(status -> {
            assertNotNull(item.getId());
            assertEquals(ORDERED, itemRepository.findById(item.getId()).orElseThrow()
                    .getMemberSeminarItemStatus());
            List<MemberSeminarItemStatusHistory> history = historyService.findByMemberSeminarItemId(item.getId());
            assertEquals(1, history.size());
            assertEquals(ORDERED, history.getFirst().getToStatus());
            assertEquals(item.getId(), history.getFirst().getMemberSeminarItem().getId());
            assertNotNull(history.getFirst().getCreatedAt());
        });
    }

    @Test
    void appendsPaidHistoryAfterTheInitialOrderedHistory() {
        MemberSeminarItem item = createPaidItem();

        transactions.executeWithoutResult(status -> {
            assertEquals(PAID, itemRepository.findById(item.getId()).orElseThrow()
                    .getMemberSeminarItemStatus());
            assertEquals(List.of(ORDERED, PAID), historyService.findByMemberSeminarItemId(item.getId())
                    .stream().map(MemberSeminarItemStatusHistory::getToStatus).toList());
        });
    }

    @Test
    void fullCancelRecordsEveryItemIncludingBeforeTheOrderIsFullyCancelled() {
        MemberSeminarItem first = createPaidItem();
        MemberSeminarItem second = createPaidItem();

        var results = useCase.cancelAll(orderId, List.of(first.getId(), second.getId()));

        assertEquals(2, results.size());
        results.forEach(result -> assertTrue(result.isSuccess()));
        transactions.executeWithoutResult(status -> {
            assertCancelledWithHistory(first.getId());
            assertCancelledWithHistory(second.getId());
            assertEquals(FULL_CANCELED, orderRepository.findById(orderId).orElseThrow().getOrderStatus());
        });
    }

    @Test
    void partialCancelRecordsHistoryForPartialAndFullyCancelledOrders() {
        MemberSeminarItem first = createPaidItem();
        MemberSeminarItem second = createPaidItem();

        assertTrue(useCase.cancelPartial(orderId, List.of(first.getId())).getFirst().isSuccess());

        transactions.executeWithoutResult(status -> {
            assertCancelledWithHistory(first.getId());
            assertEquals(PAID, itemRepository.findById(second.getId()).orElseThrow()
                    .getMemberSeminarItemStatus());
            assertEquals(PARTIAL_CANCELLED, orderRepository.findById(orderId).orElseThrow().getOrderStatus());
        });

        assertTrue(useCase.cancelPartial(orderId, List.of(second.getId())).getFirst().isSuccess());

        transactions.executeWithoutResult(status -> {
            assertCancelledWithHistory(second.getId());
            assertEquals(FULL_CANCELED, orderRepository.findById(orderId).orElseThrow().getOrderStatus());
        });
    }

    @Test
    void rollsBackStatusChangeWhenTheActionCannotStoreHistory() {
        MemberSeminarItem item = useCase.create(orderId, seminarId);
        recordSuccessfulPayment(item.getId());
        doThrow(new DataIntegrityViolationException("history insert failed"))
                .when(historyRepository).save(any(MemberSeminarItemStatusHistory.class));

        assertThrows(TransitionException.class, () -> useCase.confirmPayment(orderId, item.getId()));

        transactions.executeWithoutResult(status -> {
            assertEquals(ORDERED, itemRepository.findById(item.getId()).orElseThrow()
                    .getMemberSeminarItemStatus());
            assertEquals(List.of(ORDERED), historyService.findByMemberSeminarItemId(item.getId())
                    .stream().map(MemberSeminarItemStatusHistory::getToStatus).toList());
        });
    }

    @Test
    void rollsBackTheCreatedItemWhenInitialHistoryCannotBeStored() {
        long itemCount = itemRepository.count();
        long historyCount = historyRepository.count();
        doThrow(new DataIntegrityViolationException("history insert failed"))
                .when(historyRepository).save(any(MemberSeminarItemStatusHistory.class));

        assertThrows(TransitionException.class, () -> useCase.create(orderId, seminarId));

        transactions.executeWithoutResult(status -> {
            assertEquals(itemCount, itemRepository.count());
            assertEquals(historyCount, historyRepository.count());
        });
    }

    @Test
    void rollsBackItemAndHistoryWhenTheOuterTransactionFails() {
        AtomicReference<Long> createdItemId = new AtomicReference<>();

        assertThrows(IllegalStateException.class, () -> transactions.executeWithoutResult(status -> {
            createdItemId.set(useCase.create(orderId, seminarId).getId());
            throw new IllegalStateException("later operation failed");
        }));

        transactions.executeWithoutResult(status -> {
            assertTrue(itemRepository.findById(createdItemId.get()).isEmpty());
            assertTrue(historyService.findByMemberSeminarItemId(createdItemId.get()).isEmpty());
        });
    }

    @Test
    void refusesHistoryWritesWithoutAnExistingTransaction() {
        assertThrows(IllegalTransactionStateException.class,
                () -> historyService.record(MemberSeminarItem.builder().build(), ORDERED));
    }

    private MemberSeminarItem createPaidItem() {
        MemberSeminarItem item = useCase.create(orderId, seminarId);
        recordSuccessfulPayment(item.getId());
        assertTrue(useCase.confirmPayment(orderId, item.getId()).isSuccess());
        return item;
    }

    private void recordSuccessfulPayment(Long itemId) {
        transactions.executeWithoutResult(status -> paymentRepository.save(Payment.builder()
                .memberSeminarItem(itemRepository.findById(itemId).orElseThrow())
                .paymentAmount(BigDecimal.TEN)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build()));
    }

    private void assertCancelledWithHistory(Long itemId) {
        assertEquals(CANCELLED, itemRepository.findById(itemId).orElseThrow()
                .getMemberSeminarItemStatus());
        assertEquals(List.of(ORDERED, PAID, CANCELLED), historyService.findByMemberSeminarItemId(itemId)
                .stream().map(MemberSeminarItemStatusHistory::getToStatus).toList());
    }
}
