package com.seminarhub.global.error;

import com.seminarhub.dto.ErrorResponse;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.error.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BatchExceptionHandler {

    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<ErrorResponse> handleApplicationError(ErrorResponse exception) {
        log.warn("Batch API error. code={}, message={}", exception.getCode(), exception.getMessage(), exception);
        return exception.toResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception) {
        log.warn("Batch bad request. message={}", exception.getMessage(), exception);
        return new BadRequestException(exception.getMessage(), exception).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unexpected batch API error. message={}", exception.getMessage(), exception);
        return new InternalServerErrorException(exception.getMessage(), exception).toResponseEntity();
    }
}
