package com.seminarhub.error;

import com.seminarhub.dto.ErrorResponse;
import com.seminarhub.error.origin.ErrorOrigin.CommonError;
import com.seminarhub.error.origin.Origin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class InternalServerErrorException extends ErrorResponse {

    private static final Origin origin = CommonError.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(String message) {
        super(origin, message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(origin, message, cause);
    }

    public InternalServerErrorException(Origin origin, String message) {
        super(origin, message);
    }

    public InternalServerErrorException(Origin origin, String message, Throwable cause) {
        super(origin, message, cause);
    }

    @Override
    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
    }
}
