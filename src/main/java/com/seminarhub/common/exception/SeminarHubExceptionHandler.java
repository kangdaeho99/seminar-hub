package com.seminarhub.common.exception;


import javassist.bytecode.DuplicateMemberException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class SeminarHubExceptionHandler {

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

    @ExceptionHandler(value = DuplicateMemberException.class)
    public ResponseEntity<ErrorResponseDTO> DuplicateMemberExceptionHandler(Exception e){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        log.info("-------------------Global DuplicateMemberExceptionHandler-------------------");

        errorResponseDTO.setErrorType(httpStatus.getReasonPhrase());
        errorResponseDTO.setErrorCode(String.valueOf(httpStatus.value()));
        errorResponseDTO.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponseDTO, responseHeaders, httpStatus);
    }

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Map<String, String>> ExceptionHandler(Exception e){
//        HttpHeaders responseHeaders = new HttpHeaders();
//        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
//        log.info("-------------------Global ExceptionHandler-------------------");
//
//        Map<String, String> map = new HashMap<>();
//
//        map.put("error type", httpStatus.getReasonPhrase());
//        map.put("code", String.valueOf(httpStatus.value()));
//        map.put("message", e.getMessage()+ " "+ e.toString());
//
//        return new ResponseEntity<>(map, responseHeaders, httpStatus);
//    }
}
