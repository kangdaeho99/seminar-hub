package com.seminarhub.common.exception;


import javassist.bytecode.DuplicateMemberException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * [ 2023-08-10 daeho.kang ]
 * Description : SeminarHub에서 사용하는 Custom Excpeion 입니다.
 * 기본적으로 Java에서 제공하는 Exception 을 사용하지만, 해당 Exception 이 존재하지 않을시 새로운 에러객체를 만들어 진행합니다.
 * ( Exception 이 없을시, Exception 패키지에 새로 해당 클래스를 만듭니다. )
 *
 */
@RestControllerAdvice
@Log4j2
public class SeminarHubExceptionHandler {

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : 기본 Exception 발생 시
     *
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
     * Description : 중복되는 회원 Exception 발생 시
     *
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
     * Description : 중복되는 Seminar Exception 발생 시
     *
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
