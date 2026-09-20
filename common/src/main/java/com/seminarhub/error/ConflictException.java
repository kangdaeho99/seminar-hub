package com.seminarhub.error;

import com.seminarhub.dto.ErrorResponse;
import com.seminarhub.error.origin.ErrorOrigin.CommonError;
import com.seminarhub.error.origin.Origin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ConflictException extends ErrorResponse {

    private static final Origin origin = CommonError.CONFLICT;

    public ConflictException() {
        super(origin);
    }

    public ConflictException(String message) {
        super(origin, message);
    }

    public ConflictException(String message, Throwable cause) {
        super(origin, message, cause);
    }

    public ConflictException(Origin origin) {
        super(origin, origin.getMessage());
    }

    @Override
    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(this);
    }
}
