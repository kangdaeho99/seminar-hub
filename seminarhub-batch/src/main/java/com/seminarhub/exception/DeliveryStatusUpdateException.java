package com.seminarhub.exception;

public class DeliveryStatusUpdateException extends RuntimeException {
    public DeliveryStatusUpdateException(String message) {
        super(message);
    }

    public DeliveryStatusUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
