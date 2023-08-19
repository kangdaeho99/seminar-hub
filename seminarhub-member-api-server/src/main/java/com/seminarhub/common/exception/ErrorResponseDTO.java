package com.seminarhub.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * [ 2023-08-10 daeho.kang ]
 * Description : 일관적인 Error 처리를 위한 ErrorResponseDTO 입니다.
 * errorType : 에러 타입 ( ex. Validation Error )
 * errorCode : 에러 코드 ( ex. 400 )
 * errorMessage : 에러 메세지 ( ex. Invalid input data )
 */
@Getter
@Setter
public class ErrorResponseDTO {
    private String errorType;
    private String errorCode;
    private String errorMessage;


}
