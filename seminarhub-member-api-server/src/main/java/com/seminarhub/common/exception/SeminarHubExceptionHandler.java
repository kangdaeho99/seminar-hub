package com.seminarhub.common.exception;


import com.seminarhub.feign.exception.FeignServerErrorException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * [ 2023-08-10 daeho.kang ]
 * Description : Custom exception handler used in SeminarHub.
 * While primarily utilizing standard Java exceptions, it creates new error objects if the specific exception is absent.
 * (If the exception does not exist, you should create new exception class in the Exception package.)
 */
@RestControllerAdvice
@Log4j2
public class SeminarHubExceptionHandler {

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : Handling basic exceptions
     * This handler is mainly used when creating exceptions that inherit from the Exception class.
     */
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


    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : Handling duplicate member exceptions
     * This handler is used when DuplicateMemberException occurs.
     */
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

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : Handling duplicate seminar exceptions
     * This handler is used when DuplicateSeminarException occurs.
     */
    @ExceptionHandler(value = DuplicateSeminarException.class)
    public ResponseEntity<ErrorResponseDTO> DuplicateSeminarException(Exception e){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        log.info("-------------------Global DuplicateSeminarExceptionHandler-------------------");

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
