package com.seminarhub.global.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Swagger에 ID 커서 조회 파라미터를 노출한다. */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        name = "size",
        in = ParameterIn.QUERY,
        description = "조회 크기",
        schema = @Schema(type = "integer", defaultValue = "10", minimum = "1", maximum = "500"))
@Parameter(
        name = "cursor",
        in = ParameterIn.QUERY,
        description = "마지막으로 조회한 정산 ID",
        schema = @Schema(type = "integer", format = "int64", minimum = "1"))
@Parameter(
        name = "direction",
        in = ParameterIn.QUERY,
        description = "조회 방향",
        schema = @Schema(type = "string", defaultValue = "DESC", allowableValues = {"ASC", "DESC"}))
public @interface CursorSwagger {}
