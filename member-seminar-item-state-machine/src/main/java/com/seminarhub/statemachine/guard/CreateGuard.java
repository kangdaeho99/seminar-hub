package com.seminarhub.statemachine.guard;

import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.result.TransitionRejectionCode;

@Component
public class CreateGuard extends AnonymousCondition<TransitionExecutionContext> {

    @Override
    public boolean isSatisfied(TransitionExecutionContext context) {
        if (context.getRequest().item().getId() != null) {
            return context.reject(TransitionRejectionCode.ITEM_ALREADY_CREATED);
        }
        return true;
    }
}
