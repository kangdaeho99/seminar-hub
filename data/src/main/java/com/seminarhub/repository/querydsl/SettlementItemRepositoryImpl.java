package com.seminarhub.repository.querydsl;

import static com.seminarhub.entity.QSettlementItem.settlementItem;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.global.dto.CursorRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
public class SettlementItemRepositoryImpl implements SettlementItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SettlementItem> search(SettlementItemSearchQuery query) {
        return queryFactory.selectFrom(settlementItem)
                .where(searchCondition(query))
                .orderBy(settlementItem.id.desc())
                .fetch();
    }

    @Override
    public Page<SettlementItem> search(SettlementItemSearchQuery query, Pageable pageable) {
        List<SettlementItem> content = queryFactory.selectFrom(settlementItem)
                .where(searchCondition(query))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers(pageable))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(settlementItem.count())
                .from(settlementItem)
                .where(searchCondition(query));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<SettlementItem> search(SettlementItemSearchQuery query, CursorRequest cursorRequest) {
        return queryFactory.selectFrom(settlementItem)
                .where(searchCondition(query), cursorRequest.cursorCondition(settlementItem.id))
                .orderBy(cursorRequest.orderBy(settlementItem.id))
                .limit((long) cursorRequest.getSize() + 1)
                .fetch();
    }

    private BooleanBuilder searchCondition(SettlementItemSearchQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!query.hasDeletedAtCondition()) builder.and(settlementItem.deleted_at.isNull());
        if (query.id() != null) builder.and(settlementItem.id.eq(query.id()));
        if (!CollectionUtils.isEmpty(query.ids())) builder.and(settlementItem.id.in(query.ids()));
        if (query.settlementId() != null) builder.and(settlementItem.settlement.id.eq(query.settlementId()));
        if (!CollectionUtils.isEmpty(query.settlementIds())) {
            builder.and(settlementItem.settlement.id.in(query.settlementIds()));
        }
        if (query.memberSeminarId() != null) {
            builder.and(settlementItem.memberSeminar.id.eq(query.memberSeminarId()));
        }
        if (!CollectionUtils.isEmpty(query.memberSeminarIds())) {
            builder.and(settlementItem.memberSeminar.id.in(query.memberSeminarIds()));
        }
        if (query.amount() != null) builder.and(settlementItem.amount.eq(query.amount()));
        if (query.amountMin() != null) builder.and(settlementItem.amount.goe(query.amountMin()));
        if (query.amountMax() != null) builder.and(settlementItem.amount.loe(query.amountMax()));
        if (query.createdAt() != null) builder.and(settlementItem.inst_dt.eq(query.createdAt()));
        if (query.updatedAt() != null) builder.and(settlementItem.updt_dt.eq(query.updatedAt()));
        if (query.deletedAt() != null) builder.and(settlementItem.deleted_at.eq(query.deletedAt()));
        if (query.createdStartAt() != null) builder.and(settlementItem.inst_dt.goe(query.createdStartAt()));
        if (query.createdEndAt() != null) builder.and(settlementItem.inst_dt.loe(query.createdEndAt()));
        if (query.updatedStartAt() != null) builder.and(settlementItem.updt_dt.goe(query.updatedStartAt()));
        if (query.updatedEndAt() != null) builder.and(settlementItem.updt_dt.loe(query.updatedEndAt()));
        if (query.deletedStartAt() != null) builder.and(settlementItem.deleted_at.goe(query.deletedStartAt()));
        if (query.deletedEndAt() != null) builder.and(settlementItem.deleted_at.loe(query.deletedEndAt()));
        return builder;
    }

    private OrderSpecifier<?>[] orderSpecifiers(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) return defaultOrder();
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        boolean hasIdOrder = false;
        for (Sort.Order order : pageable.getSort()) {
            ComparableExpressionBase<?> expression = sortExpression(order.getProperty());
            if (expression == null) return defaultOrder();
            orders.add(order.isAscending() ? expression.asc() : expression.desc());
            hasIdOrder |= "id".equals(order.getProperty());
        }
        if (!hasIdOrder) orders.add(settlementItem.id.desc());
        return orders.toArray(OrderSpecifier[]::new);
    }

    private ComparableExpressionBase<?> sortExpression(String property) {
        return switch (property) {
            case "id" -> settlementItem.id;
            case "settlementId" -> settlementItem.settlement.id;
            case "memberSeminarId" -> settlementItem.memberSeminar.id;
            case "amount" -> settlementItem.amount;
            case "createdAt" -> settlementItem.inst_dt;
            case "updatedAt" -> settlementItem.updt_dt;
            case "deletedAt" -> settlementItem.deleted_at;
            default -> null;
        };
    }

    private OrderSpecifier<?>[] defaultOrder() {
        return new OrderSpecifier<?>[] {settlementItem.id.desc()};
    }
}
