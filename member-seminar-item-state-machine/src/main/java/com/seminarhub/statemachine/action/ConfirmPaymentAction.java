package com.seminarhub.statemachine.action;

import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousAction;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.MemberSeminarItemStateMachine;
import com.seminarhub.statemachine.context.TransitionExecutionContext;

@Component
public class ConfirmPaymentAction
        extends AnonymousAction<MemberSeminarItemStateMachine, MemberSeminarItemStatus,
                MemberSeminarItemEvent, TransitionExecutionContext> {

    @Override
    public void execute(
            MemberSeminarItemStatus from,
            MemberSeminarItemStatus to,
            MemberSeminarItemEvent event,
            TransitionExecutionContext context,
            MemberSeminarItemStateMachine stateMachine
    ) {
        context.getRequest().item().changeStatus(to);
    }
}
