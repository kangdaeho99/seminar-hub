package com.seminarhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

/** 페이지 목록과 페이지 상세 정보를 함께 반환한다. */
public record Pagination<T>(
        @Schema(description = "페이지 정보") PaginationInfo pagination,
        @Schema(description = "조회 결과") List<T> items) {

    public static <T> Pagination<T> of(Page<T> page) {
        if (page == null) return null;
        return new Pagination<>(PaginationInfo.of(page), page.getContent());
    }

    public record PaginationInfo(
            @Schema(description = "현재 페이지의 시작 인덱스", example = "0") Long startIdx,
            @Schema(description = "현재 페이지의 종료 인덱스", example = "20") Long endIdx,
            @Schema(description = "전체 항목 수", example = "100") Long total,
            @Schema(description = "페이지 크기", example = "20") Integer size,
            @Schema(description = "현재 페이지 번호(1부터 시작)", example = "1") Integer currentPage,
            @Schema(description = "마지막 페이지 번호", example = "5") Integer lastPage) {
        public static PaginationInfo of(Page<?> page) {
            long offset = page.getPageable().getOffset();
            return new PaginationInfo(
                    offset,
                    offset + page.getNumberOfElements(),
                    page.getTotalElements(),
                    page.getSize(),
                    page.getPageable().getPageNumber() + 1,
                    page.getTotalPages() == 0 ? 1 : page.getTotalPages());
        }
    }
}
