package com.seminarhub.global.dto;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.seminarhub.error.BadRequestException;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@ToString
public final class CursorRequest {

    private static final int MAX_PAGE_SIZE = 500;

    private final int size;
    private final Long cursor;
    private final Direction direction;

    private CursorRequest(int size, @Nullable Long cursor, Direction direction) {
        if (size <= 0) {
            throw new BadRequestException("조회 사이즈는 0보다 커야 합니다.");
        }
        if (cursor != null && cursor <= 0) {
            throw new BadRequestException("커서는 0보다 커야 합니다.");
        }
        this.size = Math.min(size, MAX_PAGE_SIZE);
        this.cursor = cursor;
        this.direction = Objects.requireNonNull(direction, "direction cannot be null");
    }

    public static CursorRequest of(int size, @Nullable Long cursor, Direction direction) {
        return new CursorRequest(size, cursor, direction);
    }

    public OrderSpecifier<Long> orderBy(NumberExpression<Long> expression) {
        return direction.isAscending() ? expression.asc() : expression.desc();
    }

    public BooleanExpression cursorCondition(NumberExpression<Long> expression) {
        if (cursor == null) {
            return null;
        }
        return direction.isAscending() ? expression.gt(cursor) : expression.lt(cursor);
    }
}
