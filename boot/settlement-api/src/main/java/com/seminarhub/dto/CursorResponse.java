package com.seminarhub.dto;

import java.util.List;
import java.util.function.Function;

public record CursorResponse<T>(CursorInfo cursor, List<T> items) {

    public static <T> CursorResponse<T> of(List<T> fetched, int size, Function<T, Long> cursorExtractor) {
        boolean hasNext = fetched.size() > size;
        List<T> items = fetched.stream().limit(size).toList();
        Long nextCursor = hasNext ? cursorExtractor.apply(items.get(items.size() - 1)) : null;
        return new CursorResponse<>(new CursorInfo(hasNext, nextCursor), items);
    }

    public record CursorInfo(boolean hasNext, Long nextCursor) {}
}
