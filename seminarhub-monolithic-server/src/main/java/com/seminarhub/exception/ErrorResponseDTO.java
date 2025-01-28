package com.seminarhub.exception;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String errorType;
    private String errorCode;
    private String errorMessage;
}
