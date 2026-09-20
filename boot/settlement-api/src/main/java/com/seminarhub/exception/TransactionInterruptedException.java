package com.seminarhub.exception;

import com.seminarhub.error.BadRequestException;
import com.seminarhub.error.origin.ErrorOrigin.SeminarHubError;

public class TransactionInterruptedException extends BadRequestException {
    public TransactionInterruptedException(String message, Throwable cause) {
        super(SeminarHubError.TRANSACTION_INTERRUPTED, message, cause);
    }
}
