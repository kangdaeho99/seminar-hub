package com.seminarhub.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {
    private String errorType;
    private String errorCode;
    private String errorMessage;


}
