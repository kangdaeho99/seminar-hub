package com.seminarhub.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDTO> ExceptionHandler(Exception e){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.info("-------------------Global ExceptionHandler-------------------");
        errorResponseDTO.setErrorType(httpStatus.getReasonPhrase());
        errorResponseDTO.setErrorCode(String.valueOf(httpStatus.value()));
        errorResponseDTO.setErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorResponseDTO, responseHeaders, httpStatus);
    }
}
