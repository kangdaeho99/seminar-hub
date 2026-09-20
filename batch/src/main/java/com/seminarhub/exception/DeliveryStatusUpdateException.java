package com.seminarhub.exception;

import com.seminarhub.error.InternalServerErrorException;
import com.seminarhub.error.origin.ErrorOrigin.SeminarHubError;

public class DeliveryStatusUpdateException extends InternalServerErrorException {
    public DeliveryStatusUpdateException(String message) {
        super(SeminarHubError.DELIVERY_STATUS_UPDATE_FAILED, message);
    }

    public DeliveryStatusUpdateException(String message, Throwable cause) {
        super(SeminarHubError.DELIVERY_STATUS_UPDATE_FAILED, message, cause);
    }
}
