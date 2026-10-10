package com.seminarhub.statemachine;

import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.result.TransitionRejectionCode;

public class MemberSeminarItemStateMachine
        extends AbstractStateMachine<
                MemberSeminarItemStateMachine,
                MemberSeminarItemStatus,
                MemberSeminarItemEvent,
                TransitionExecutionContext> {

    @Override
    protected void afterTransitionDeclined(
            MemberSeminarItemStatus fromState,
            MemberSeminarItemEvent event,
            TransitionExecutionContext context
    ) {
        if (!context.isRejected()) {
            context.reject(TransitionRejectionCode.TRANSITION_NOT_ALLOWED);
        }
    }
}
