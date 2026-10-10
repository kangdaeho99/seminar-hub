package com.seminarhub.statemachine.guard;

import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.result.TransitionRejectionCode;

@Component
public class PartialCancelGuard extends AnonymousCondition<TransitionExecutionContext> {

    @Override
    public boolean isSatisfied(TransitionExecutionContext context) {
        if (context.getRequest().item().getMemberSeminarItemStatus() != MemberSeminarItemStatus.PAID) {
            return context.reject(TransitionRejectionCode.TRANSITION_NOT_ALLOWED);
        }
        return true;
    }
}
