package com.seminarhub.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDTO {
    private String errorType;
    private String errorCode;
    private String errorMessage;
    private String path;
    private LocalDateTime timestamp;
}
