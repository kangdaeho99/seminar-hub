package com.seminarhub.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.statemachine.action.FullCancelAction;
import com.seminarhub.statemachine.action.ConfirmPaymentAction;
import com.seminarhub.statemachine.action.PartialCancelAction;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.guard.FullCancelGuard;
import com.seminarhub.statemachine.guard.ConfirmPaymentGuard;
import com.seminarhub.statemachine.guard.PartialCancelGuard;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MemberSeminarItemStateMachineConfiguration {

    private final ConfirmPaymentGuard confirmPaymentGuard;
    private final ConfirmPaymentAction confirmPaymentAction;
    private final FullCancelGuard fullCancelGuard;
    private final FullCancelAction fullCancelAction;
    private final PartialCancelGuard partialCancelGuard;
    private final PartialCancelAction partialCancelAction;

    @Bean
    public StateMachineBuilder<
            MemberSeminarItemStateMachine,
            MemberSeminarItemStatus,
            MemberSeminarItemEvent,
            TransitionExecutionContext> memberSeminarItemStateMachineBuilder() {
        StateMachineBuilder<
                MemberSeminarItemStateMachine,
                MemberSeminarItemStatus,
                MemberSeminarItemEvent,
                TransitionExecutionContext> builder =
                StateMachineBuilderFactory.create(
                        MemberSeminarItemStateMachine.class,
                        MemberSeminarItemStatus.class,
                        MemberSeminarItemEvent.class,
                        TransitionExecutionContext.class
                );

        builder.externalTransition()
                .from(MemberSeminarItemStatus.ORDERED)
                .to(MemberSeminarItemStatus.PAID)
                .on(MemberSeminarItemEvent.CONFIRM_PAYMENT)
                .when(confirmPaymentGuard)
                .perform(confirmPaymentAction);

        builder.externalTransition()
                .from(MemberSeminarItemStatus.PAID)
                .to(MemberSeminarItemStatus.CANCELLED)
                .on(MemberSeminarItemEvent.FULL_CANCEL)
                .when(fullCancelGuard)
                .perform(fullCancelAction);

        builder.externalTransition()
                .from(MemberSeminarItemStatus.PAID)
                .to(MemberSeminarItemStatus.CANCELLED)
                .on(MemberSeminarItemEvent.PARTIAL_CANCEL)
                .when(partialCancelGuard)
                .perform(partialCancelAction);

        return builder;
    }
}
