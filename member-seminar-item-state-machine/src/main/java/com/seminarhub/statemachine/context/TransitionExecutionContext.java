package com.seminarhub.statemachine.context;

import com.seminarhub.statemachine.result.TransitionRejectionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TransitionExecutionContext {

    private final MemberSeminarItemContext request;

    private TransitionRejectionCode rejection;

    public boolean reject(TransitionRejectionCode code) {
        if (rejection == null) {
            rejection = code;
        }
        return false;
    }

    public boolean isRejected() {
        return rejection != null;
    }
}
