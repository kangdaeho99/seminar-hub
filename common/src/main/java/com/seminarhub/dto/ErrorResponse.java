package com.seminarhub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seminarhub.error.origin.Origin;
import java.util.Objects;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

@Getter
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class ErrorResponse extends RuntimeException {

    private final String origin;
    private final int code;
    private final String message;

    protected ErrorResponse(Origin origin) {
        this(origin, origin.getCode(), origin.getMessage());
    }

    protected ErrorResponse(Origin origin, String message) {
        this(origin, origin.getCode(), message);
    }

    protected ErrorResponse(Origin origin, String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(origin, "Origin must not be null");
        this.origin = origin.getOrigin().name();
        this.code = origin.getCode();
        this.message = message;
    }

    protected ErrorResponse(Origin origin, int code, String message) {
        Assert.notNull(origin, "Origin must not be null");
        this.origin = origin.getOrigin().name();
        this.code = code;
        this.message = message;
    }

    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this);
    }

    @Override
    public String toString() {
        return "Origin: " + origin + ", Code: " + code + ", Message: "
                + Objects.requireNonNullElse(message, "") + "\n";
    }
}
