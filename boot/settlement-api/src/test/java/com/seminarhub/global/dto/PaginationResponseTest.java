package com.seminarhub.global.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class PaginationResponseTest {

    @Test
    void usesExclusiveEndOffsetAndOneBasedPageNumber() {
        Pagination<Integer> response = Pagination.of(new PageImpl<>(List.of(10, 11), PageRequest.of(1, 2), 5));
        assertEquals(2, response.pagination().startIdx());
        assertEquals(4, response.pagination().endIdx());
        assertEquals(2, response.pagination().currentPage());
        assertEquals(3, response.pagination().lastPage());
    }

    @Test
    void emptyPageHasLastPageOne() {
        Pagination<Integer> response = Pagination.of(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));
        assertEquals(1, response.pagination().lastPage());
    }

    @Test
    void cursorRemovesLookAheadItem() {
        CursorResponse<Long> next = CursorResponse.of(List.of(3L, 2L, 1L), 2, value -> value);
        assertTrue(next.cursor().hasNext());
        assertEquals(2L, next.cursor().nextCursor());
        assertEquals(List.of(3L, 2L), next.items());

        CursorResponse<Long> last = CursorResponse.of(List.of(1L), 2, value -> value);
        assertFalse(last.cursor().hasNext());
        assertNull(last.cursor().nextCursor());
    }
}
