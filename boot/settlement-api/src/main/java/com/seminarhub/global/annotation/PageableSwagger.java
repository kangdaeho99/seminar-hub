package com.seminarhub.global.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Swagger에 1-based 페이지 요청 파라미터를 노출한다. */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        description = "페이지 번호 (1부터 시작)",
        name = "page",
        schema = @Schema(type = "integer", defaultValue = "1"))
@Parameter(
        in = ParameterIn.QUERY,
        description = "페이지 크기",
        name = "size",
        schema = @Schema(type = "integer", defaultValue = "20"))
@Parameter(
        in = ParameterIn.QUERY,
        description = "정렬 조건 (field,ASC|DESC). 허용 필드: id, startDate, endDate, amount, settlementStatus, createdAt, updatedAt, deletedAt",
        name = "sort",
        schema = @Schema(type = "string", defaultValue = "id,DESC", example = "startDate,ASC"))
public @interface PageableSwagger {}
