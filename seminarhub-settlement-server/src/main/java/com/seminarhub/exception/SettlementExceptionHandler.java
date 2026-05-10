package com.seminarhub.exception;

import com.seminarhub.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class SettlementExceptionHandler {

    @ExceptionHandler(SettlementDateNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleSettlementDateNotFound(
            SettlementDateNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler({
            PessimisticLockingFailureException.class,
            CannotAcquireLockException.class,
            CannotSerializeTransactionException.class,
            CannotCreateTransactionException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleConcurrencyException(
            Exception exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.CONFLICT, exception, request);
    }

    @ExceptionHandler({
            TransactionInterruptedException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(
            Exception exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status,
            Exception exception,
            HttpServletRequest request
    ) {
        log.warn("Settlement concurrency API error. status={}, message={}", status.value(), exception.getMessage(), exception);

        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .errorType(status.getReasonPhrase())
                .errorCode(String.valueOf(status.value()))
                .errorMessage(exception.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
