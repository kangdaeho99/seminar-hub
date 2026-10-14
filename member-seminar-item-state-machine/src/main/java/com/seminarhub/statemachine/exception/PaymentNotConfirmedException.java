package com.seminarhub.statemachine.exception;

import com.seminarhub.statemachine.result.TransitionRejectionCode;

public final class PaymentNotConfirmedException extends RuntimeException {

    private final Long itemId;

    public PaymentNotConfirmedException(Long itemId) {
        super(TransitionRejectionCode.PAYMENT_NOT_CONFIRMED.getMessage());
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public TransitionRejectionCode getCode() {
        return TransitionRejectionCode.PAYMENT_NOT_CONFIRMED;
    }
}
