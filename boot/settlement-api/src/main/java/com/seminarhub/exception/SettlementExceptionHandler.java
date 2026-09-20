package com.seminarhub.exception;

import com.seminarhub.dto.ErrorResponse;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.error.ConflictException;
import com.seminarhub.error.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SettlementExceptionHandler {

    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<ErrorResponse> handleApplicationError(ErrorResponse exception) {
        log.warn("Settlement API error. code={}, message={}", exception.getCode(), exception.getMessage(), exception);
        return exception.toResponseEntity();
    }

    @ExceptionHandler({
            PessimisticLockingFailureException.class,
            CannotAcquireLockException.class,
            CannotSerializeTransactionException.class,
            CannotCreateTransactionException.class
    })
    public ResponseEntity<ErrorResponse> handleConcurrencyException(Exception exception) {
        log.warn("Settlement concurrency error. message={}", exception.getMessage(), exception);
        return new ConflictException(exception.getMessage(), exception).toResponseEntity();
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception) {
        log.warn("Settlement bad request. message={}", exception.getMessage(), exception);
        return new BadRequestException(exception.getMessage(), exception).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unexpected settlement API error. message={}", exception.getMessage(), exception);
        return new InternalServerErrorException(exception.getMessage(), exception).toResponseEntity();
    }
}
