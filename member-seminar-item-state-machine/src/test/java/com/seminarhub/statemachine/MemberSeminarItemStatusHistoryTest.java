package com.seminarhub.statemachine;

import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.ORDERED;
import static com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus.PAID;
import static com.seminarhub.statemachine.event.MemberSeminarItemEvent.CONFIRM_PAYMENT;
import static com.seminarhub.statemachine.event.MemberSeminarItemEvent.CREATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.squirrelframework.foundation.exception.TransitionException;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;

import com.seminarhub.domain.payment.service.PaymentService;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.domain.seminar.service.MemberSeminarItemStatusHistoryService;
import com.seminarhub.statemachine.action.ConfirmPaymentAction;
import com.seminarhub.statemachine.action.CreateAction;
import com.seminarhub.statemachine.action.FullCancelAction;
import com.seminarhub.statemachine.action.PartialCancelAction;
import com.seminarhub.statemachine.context.ConfirmPaymentContext;
import com.seminarhub.statemachine.context.CreateContext;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.exception.PaymentNotConfirmedException;
import com.seminarhub.statemachine.guard.ConfirmPaymentGuard;
import com.seminarhub.statemachine.guard.CreateGuard;
import com.seminarhub.statemachine.guard.FullCancelGuard;
import com.seminarhub.statemachine.guard.PartialCancelGuard;
import com.seminarhub.statemachine.result.TransitionRejectionCode;

class MemberSeminarItemStatusHistoryTest {

    private final MemberSeminarItemStatusHistoryService historyService =
            mock(MemberSeminarItemStatusHistoryService.class);
    private final PaymentService paymentService = mock(PaymentService.class);
    private final MemberSeminarItemService itemService = mock(MemberSeminarItemService.class);

    @Test
    void recordsInitialOrderedHistoryAfterSavingANewItem() {
        MemberSeminarItem item = MemberSeminarItem.builder().build();
        var context = new TransitionExecutionContext(new CreateContext(item));
        var machine = configuredBuilder().newStateMachine(ORDERED);

        machine.fire(CREATE, context);

        assertEquals(ORDERED, machine.getCurrentState());
        var calls = inOrder(itemService, historyService);
        calls.verify(itemService).save(item);
        calls.verify(historyService).record(item, ORDERED);
        calls.verifyNoMoreInteractions();
    }

    @Test
    void doesNotRecordAnotherCreationForAnExistingItem() {
        MemberSeminarItem item = item(ORDERED);
        var context = new TransitionExecutionContext(new CreateContext(item));

        configuredBuilder().newStateMachine(ORDERED).fire(CREATE, context);

        assertEquals(TransitionRejectionCode.ITEM_ALREADY_CREATED, context.getRejection());
        verifyNoInteractions(itemService, historyService);
    }

    @Test
    void doesNotRecordInitialHistoryWhenSavingTheItemFails() {
        MemberSeminarItem item = MemberSeminarItem.builder().build();
        when(itemService.save(item)).thenThrow(new IllegalStateException("item storage failed"));
        var machine = configuredBuilder().newStateMachine(ORDERED);

        assertThrows(TransitionException.class, () -> machine.fire(
                CREATE, new TransitionExecutionContext(new CreateContext(item))));

        verifyNoInteractions(historyService);
    }

    @Test
    void recordsConfiguredExternalTransitionOnceAfterTheItemChanges() {
        MemberSeminarItem item = item(ORDERED);
        when(paymentService.existsSuccessfulPayment(item.getId())).thenReturn(true);
        when(historyService.record(item, PAID)).thenAnswer(invocation -> {
            assertEquals(PAID, item.getMemberSeminarItemStatus());
            return null;
        });
        MemberSeminarItemStateMachine machine = configuredBuilder().newStateMachine(ORDERED);

        machine.fire(CONFIRM_PAYMENT, context(item));

        assertEquals(PAID, machine.getCurrentState());
        verify(historyService).record(item, PAID);
        verifyNoMoreInteractions(historyService);
    }

    @Test
    void recordsInternalTransitionEvenWhenStatusDoesNotChange() {
        MemberSeminarItem item = item(PAID);
        var builder = builder();
        builder.internalTransition().within(PAID).on(CONFIRM_PAYMENT)
                .perform(new ConfirmPaymentAction(historyService));

        builder.newStateMachine(PAID).fire(CONFIRM_PAYMENT, context(item));

        verify(historyService).record(item, PAID);
        verifyNoMoreInteractions(historyService);
    }

    @Test
    void recordsExternalSelfTransitionOnce() {
        MemberSeminarItem item = item(PAID);
        var builder = builder();
        builder.externalTransition().from(PAID).to(PAID).on(CONFIRM_PAYMENT)
                .perform(new ConfirmPaymentAction(historyService));

        builder.newStateMachine(PAID).fire(CONFIRM_PAYMENT, context(item));

        verify(historyService).record(item, PAID);
        verifyNoMoreInteractions(historyService);
    }

    @Test
    void propagatesPaymentGuardExceptionWithoutChangingStatusOrRecordingHistory() {
        MemberSeminarItem item = item(ORDERED);
        TransitionExecutionContext context = context(item);

        TransitionException exception = assertThrows(TransitionException.class,
                () -> configuredBuilder().newStateMachine(ORDERED).fire(CONFIRM_PAYMENT, context));

        PaymentNotConfirmedException rejection = assertInstanceOf(
                PaymentNotConfirmedException.class, exception.getTargetException());
        assertEquals(item.getId(), rejection.getItemId());
        assertEquals(TransitionRejectionCode.PAYMENT_NOT_CONFIRMED, rejection.getCode());
        assertEquals(TransitionRejectionCode.PAYMENT_NOT_CONFIRMED.getMessage(), rejection.getMessage());
        assertNull(context.getRejection());
        assertEquals(ORDERED, item.getMemberSeminarItemStatus());
        verifyNoInteractions(historyService);
    }

    @Test
    void doesNotRecordWhenNoTransitionIsAllowed() {
        MemberSeminarItem item = item(PAID);
        TransitionExecutionContext context = context(item);

        configuredBuilder().newStateMachine(PAID).fire(CONFIRM_PAYMENT, context);

        assertEquals(TransitionRejectionCode.TRANSITION_NOT_ALLOWED, context.getRejection());
        verifyNoInteractions(historyService);
    }

    @Test
    void doesNotRecordWhenTransitionActionFails() {
        MemberSeminarItem item = mock(MemberSeminarItem.class);
        doThrow(new IllegalStateException("action failed")).when(item).changeStatus(PAID);
        var builder = builder();
        builder.externalTransition().from(ORDERED).to(PAID).on(CONFIRM_PAYMENT)
                .perform(new ConfirmPaymentAction(historyService));
        var machine = builder.newStateMachine(ORDERED);

        assertThrows(TransitionException.class, () -> machine.fire(CONFIRM_PAYMENT, context(item)));

        verifyNoInteractions(historyService);
    }

    @Test
    void propagatesHistoryFailureToTheCaller() {
        MemberSeminarItem item = item(ORDERED);
        when(paymentService.existsSuccessfulPayment(item.getId())).thenReturn(true);
        IllegalStateException failure = new IllegalStateException("history storage failed");
        when(historyService.record(item, PAID)).thenThrow(failure);
        var machine = configuredBuilder().newStateMachine(ORDERED);

        TransitionException exception = assertThrows(TransitionException.class,
                () -> machine.fire(CONFIRM_PAYMENT, context(item)));

        assertSame(failure, exception.getTargetException());
    }

    @Test
    void dryRunDoesNotChangeTheItemOrWriteHistory() {
        MemberSeminarItem item = item(ORDERED);
        when(paymentService.existsSuccessfulPayment(item.getId())).thenReturn(true);
        var machine = configuredBuilder().newStateMachine(ORDERED);

        assertEquals(PAID, machine.test(CONFIRM_PAYMENT, context(item)));

        assertEquals(ORDERED, item.getMemberSeminarItemStatus());
        verifyNoInteractions(historyService);
    }

    private StateMachineBuilder<MemberSeminarItemStateMachine, MemberSeminarItemStatus,
            MemberSeminarItemEvent, TransitionExecutionContext> configuredBuilder() {
        return new MemberSeminarItemStateMachineConfiguration(
                new CreateGuard(), new CreateAction(itemService, historyService),
                new ConfirmPaymentGuard(paymentService), new ConfirmPaymentAction(historyService),
                new FullCancelGuard(itemService), new FullCancelAction(itemService, historyService),
                new PartialCancelGuard(), new PartialCancelAction(itemService, historyService)
        ).memberSeminarItemStateMachineBuilder();
    }

    private StateMachineBuilder<MemberSeminarItemStateMachine, MemberSeminarItemStatus,
            MemberSeminarItemEvent, TransitionExecutionContext> builder() {
        return StateMachineBuilderFactory.create(
                MemberSeminarItemStateMachine.class, MemberSeminarItemStatus.class,
                MemberSeminarItemEvent.class, TransitionExecutionContext.class);
    }

    private MemberSeminarItem item(MemberSeminarItemStatus status) {
        return MemberSeminarItem.builder().id(1L).memberSeminarItemStatus(status).build();
    }

    private TransitionExecutionContext context(MemberSeminarItem item) {
        return new TransitionExecutionContext(new ConfirmPaymentContext(item));
    }
}
