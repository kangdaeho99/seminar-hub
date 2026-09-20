package com.seminarhub.error;

import com.seminarhub.dto.ErrorResponse;
import com.seminarhub.error.origin.ErrorOrigin.CommonError;
import com.seminarhub.error.origin.Origin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NotFoundException extends ErrorResponse {

    private static final Origin origin = CommonError.NOT_FOUND;

    public NotFoundException() {
        super(origin);
    }

    public NotFoundException(String message) {
        super(origin, message);
    }

    public NotFoundException(Origin origin, String message) {
        super(origin, message);
    }

    @Override
    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this);
    }
}
