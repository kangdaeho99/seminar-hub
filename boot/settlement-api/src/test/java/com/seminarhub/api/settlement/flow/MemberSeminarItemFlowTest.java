package com.seminarhub.api.settlement.flow;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.seminarhub.api.settlement.usecase.MemberSeminarItemPaymentExecutor;
import com.seminarhub.api.settlement.usecase.MemberSeminarItemUseCase;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.statemachine.exception.PaymentNotConfirmedException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MemberSeminarItemFlowTest {

    private final MemberSeminarItemUseCase useCase = mock(MemberSeminarItemUseCase.class);
    private final MemberSeminarItemPaymentExecutor executor = mock(MemberSeminarItemPaymentExecutor.class);
    private final MemberSeminarItemFlow flow = new MemberSeminarItemFlow(useCase, executor);

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void rejectsInvalidInputBeforeAnyItemCanCommit(Long orderId, List<Long> itemIds) {
        assertThrows(BadRequestException.class, () -> flow.confirmPaymentsPartial(orderId, itemIds));
        verifyNoInteractions(executor, useCase);
    }

    static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(null, List.of(1L)),
                Arguments.of(0L, List.of(1L)),
                Arguments.of(-1L, List.of(1L)),
                Arguments.of(1L, null),
                Arguments.of(1L, List.of()),
                Arguments.of(1L, Arrays.asList(1L, null)),
                Arguments.of(1L, List.of(1L, 0L)),
                Arguments.of(1L, List.of(1L, -1L)));
    }

    @Test
    void doesNotAttributeAnotherItemsPaymentFailureToTheCurrentItem() {
        PaymentNotConfirmedException exception = new PaymentNotConfirmedException(2L);
        when(executor.confirmPayment(1L, 1L)).thenThrow(exception);

        assertSame(exception, assertThrows(PaymentNotConfirmedException.class,
                () -> flow.confirmPaymentsPartial(1L, List.of(1L))));
    }
}
