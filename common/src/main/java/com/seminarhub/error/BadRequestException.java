package com.seminarhub.error;

import com.seminarhub.dto.ErrorResponse;
import com.seminarhub.error.origin.ErrorOrigin.CommonError;
import com.seminarhub.error.origin.Origin;

public class BadRequestException extends ErrorResponse {

    private static final Origin origin = CommonError.BAD_REQUEST;

    public BadRequestException() {
        super(origin);
    }

    public BadRequestException(String message) {
        super(origin, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(origin, message, cause);
    }

    public BadRequestException(Origin origin) {
        super(origin, origin.getMessage());
    }

    public BadRequestException(Origin origin, String message) {
        super(origin, message);
    }

    public BadRequestException(Origin origin, String message, Throwable cause) {
        super(origin, message, cause);
    }

    public BadRequestException(Origin origin, Throwable cause) {
        super(origin, origin.getMessage(), cause);
    }
}
