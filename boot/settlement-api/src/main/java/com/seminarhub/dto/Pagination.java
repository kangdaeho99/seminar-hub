package com.seminarhub.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record Pagination<T>(PaginationInfo pagination, List<T> items) {

    public static <T> Pagination<T> of(Page<T> page) {
        if (page == null) return null;
        return new Pagination<>(PaginationInfo.of(page), page.getContent());
    }

    public record PaginationInfo(
            Long startIdx,
            Long endIdx,
            Long total,
            Integer size,
            Integer currentPage,
            Integer lastPage) {
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
