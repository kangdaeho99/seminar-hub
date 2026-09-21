package com.seminarhub.global.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.global.dto.CursorRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;

public abstract class QuerydslRepositorySupport<E> {

    private final JPAQueryFactory queryFactory;
    private final EntityPath<E> entity;
    protected final NumberPath<Long> id;
    protected final DateTimePath<LocalDateTime> createdAt;
    protected final DateTimePath<LocalDateTime> updatedAt;
    protected final DateTimePath<LocalDateTime> deletedAt;

    protected QuerydslRepositorySupport(
            JPAQueryFactory queryFactory,
            EntityPath<E> entity,
            NumberPath<Long> id,
            DateTimePath<LocalDateTime> createdAt,
            DateTimePath<LocalDateTime> updatedAt,
            DateTimePath<LocalDateTime> deletedAt) {
        this.queryFactory = queryFactory;
        this.entity = entity;
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    protected List<E> findAll(BooleanBuilder condition) {
        return queryFactory.selectFrom(entity).where(condition).orderBy(id.desc()).fetch();
    }

    protected Page<E> findAll(
            BooleanBuilder condition, Pageable pageable, Function<String, ComparableExpressionBase<?>> sortExpression) {
        List<E> content = queryFactory
                .selectFrom(entity)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers(pageable, sortExpression))
                .fetch();
        JPAQuery<Long> countQuery = queryFactory.select(id.count()).from(entity).where(condition);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    protected List<E> findByCursor(BooleanBuilder condition, CursorRequest cursorRequest) {
        return queryFactory
                .selectFrom(entity)
                .where(condition, cursorRequest.cursorCondition(id))
                .orderBy(cursorRequest.orderBy(id))
                .limit((long) cursorRequest.getSize() + 1)
                .fetch();
    }

    protected BooleanBuilder auditCondition(AuditSearchQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!query.hasDeletedAtCondition()) builder.and(deletedAt.isNull());
        if (query.id() != null) builder.and(id.eq(query.id()));
        if (!CollectionUtils.isEmpty(query.ids())) builder.and(id.in(query.ids()));
        if (query.createdAt() != null) builder.and(createdAt.eq(query.createdAt()));
        if (query.updatedAt() != null) builder.and(updatedAt.eq(query.updatedAt()));
        if (query.deletedAt() != null) builder.and(deletedAt.eq(query.deletedAt()));
        if (query.createdStartAt() != null) builder.and(createdAt.goe(query.createdStartAt()));
        if (query.createdEndAt() != null) builder.and(createdAt.loe(query.createdEndAt()));
        if (query.updatedStartAt() != null) builder.and(updatedAt.goe(query.updatedStartAt()));
        if (query.updatedEndAt() != null) builder.and(updatedAt.loe(query.updatedEndAt()));
        if (query.deletedStartAt() != null) builder.and(deletedAt.goe(query.deletedStartAt()));
        if (query.deletedEndAt() != null) builder.and(deletedAt.loe(query.deletedEndAt()));
        return builder;
    }

    private OrderSpecifier<?>[] orderSpecifiers(
            Pageable pageable, Function<String, ComparableExpressionBase<?>> sortExpression) {
        if (pageable.getSort().isUnsorted()) return defaultOrder();
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        boolean hasId = false;
        for (Sort.Order order : pageable.getSort()) {
            ComparableExpressionBase<?> expression = sortExpression.apply(order.getProperty());
            if (expression == null) return defaultOrder();
            orders.add(order.isAscending() ? expression.asc() : expression.desc());
            hasId |= "id".equals(order.getProperty());
        }
        if (!hasId) orders.add(id.desc());
        return orders.toArray(OrderSpecifier[]::new);
    }

    private OrderSpecifier<?>[] defaultOrder() {
        return new OrderSpecifier<?>[] {id.desc()};
    }
}
