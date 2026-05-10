package com.seminarhub.exception;

public class TransactionInterruptedException extends RuntimeException {
    public TransactionInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
