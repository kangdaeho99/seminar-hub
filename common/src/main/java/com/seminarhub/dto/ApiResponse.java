package com.seminarhub.dto;

import java.util.HashMap;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@Getter
@SuppressWarnings({"unchecked", "rawtypes"})
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(Builder<T> builder) {
        this.status = builder.httpStatus.value();
        this.message = builder.message != null ? builder.message : builder.httpStatus.getReasonPhrase();
        this.data = builder.data != null ? builder.data : (T) new HashMap<>();
    }

    private static <T> Builder<T> builder(HttpStatus httpStatus) {
        return new Builder<>(httpStatus);
    }

    public static <T> ApiResponse<T> ok() {
        return ApiResponse.<T>builder(HttpStatus.OK).build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        Assert.notNull(data, "data cannot be null");
        return ApiResponse.<T>builder(HttpStatus.OK).data(data).build();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        Assert.notNull(data, "data cannot be null");
        Assert.notNull(message, "message cannot be null");
        return ApiResponse.<T>builder(HttpStatus.OK).data(data).message(message).build();
    }

    public static <T> ApiResponse<T> created() {
        return ApiResponse.<T>builder(HttpStatus.CREATED).build();
    }

    public static <T> ApiResponse<T> created(T data) {
        Assert.notNull(data, "data cannot be null");
        return ApiResponse.<T>builder(HttpStatus.CREATED).data(data).build();
    }

    public static <T> ApiResponse<T> created(String message) {
        Assert.notNull(message, "message cannot be null");
        return ApiResponse.<T>builder(HttpStatus.CREATED).message(message).build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        Assert.notNull(data, "data cannot be null");
        Assert.notNull(message, "message cannot be null");
        return ApiResponse.<T>builder(HttpStatus.CREATED).data(data).message(message).build();
    }

    public static <T> ApiResponse<T> noContent() {
        return ApiResponse.<T>builder(HttpStatus.NO_CONTENT).build();
    }

    private static class Builder<T> {

        private final HttpStatus httpStatus;
        private String message;
        private T data;

        private Builder(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }

        private Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        private Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        private ApiResponse<T> build() {
            return new ApiResponse<>(this);
        }
    }
}
