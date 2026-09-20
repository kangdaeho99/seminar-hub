package com.seminarhub.global.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.seminarhub.error.BadRequestException;
import org.junit.jupiter.api.Test;

class CursorRequestTest {

    @Test
    void capsSizeAtFiveHundred() {
        CursorRequest request = CursorRequest.of(999, null, Direction.DESC);
        assertEquals(500, request.getSize());
    }

    @Test
    void rejectsNonPositiveSizeAndCursor() {
        assertThrows(BadRequestException.class, () -> CursorRequest.of(0, null, Direction.DESC));
        assertThrows(BadRequestException.class, () -> CursorRequest.of(10, 0L, Direction.DESC));
    }
}
