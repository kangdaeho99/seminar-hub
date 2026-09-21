package com.seminarhub.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;

/** 커서 기반 목록과 다음 조회에 필요한 커서 정보를 함께 반환한다. */
public record CursorResponse<T>(
        @Schema(description = "커서 정보") CursorInfo cursor,
        @Schema(description = "조회 결과") List<T> items) {

    public static <T> CursorResponse<T> of(List<T> fetched, int size, Function<T, Long> cursorExtractor) {
        boolean hasNext = fetched.size() > size;
        List<T> items = fetched.stream().limit(size).toList();
        Long nextCursor = hasNext ? cursorExtractor.apply(items.get(items.size() - 1)) : null;
        return new CursorResponse<>(new CursorInfo(hasNext, nextCursor), items);
    }

    public record CursorInfo(
            @Schema(description = "다음 페이지 존재 여부", example = "true") boolean hasNext,
            @Schema(
                            description = "다음 페이지 조회에 사용할 커서",
                            example = "1024",
                            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                    Long nextCursor) {}
}
