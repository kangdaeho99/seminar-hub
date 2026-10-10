package com.seminarhub.statemachine.result;

import lombok.Getter;

@Getter
public final class ItemTransitionResult {

    private final Long itemId;
    private final boolean success;
    private final TransitionRejectionCode code;
    private final String message;

    public ItemTransitionResult(Long itemId, TransitionRejectionCode code) {
        this.itemId = itemId;
        this.success = code == null;
        this.code = code;
        this.message = code == null ? null : code.getMessage();
    }
}
