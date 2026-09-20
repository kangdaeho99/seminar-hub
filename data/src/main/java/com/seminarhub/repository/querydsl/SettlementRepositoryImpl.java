package com.seminarhub.repository.querydsl;

import static com.seminarhub.entity.QSettlement.settlement;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.entity.Settlement;
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
public class SettlementRepositoryImpl implements SettlementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Settlement> search(SettlementSearchQuery query) {
        return queryFactory.selectFrom(settlement)
                .where(searchCondition(query))
                .orderBy(settlement.id.desc())
                .fetch();
    }

    @Override
    public Page<Settlement> search(SettlementSearchQuery query, Pageable pageable) {
        List<Settlement> content = queryFactory.selectFrom(settlement)
                .where(searchCondition(query))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers(pageable))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(settlement.count())
                .from(settlement)
                .where(searchCondition(query));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Settlement> search(SettlementSearchQuery query, CursorRequest cursorRequest) {
        return queryFactory.selectFrom(settlement)
                .where(searchCondition(query), cursorRequest.cursorCondition(settlement.id))
                .orderBy(cursorRequest.orderBy(settlement.id))
                .limit((long) cursorRequest.getSize() + 1)
                .fetch();
    }

    private BooleanBuilder searchCondition(SettlementSearchQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!query.hasDeletedAtCondition()) builder.and(settlement.deleted_at.isNull());
        if (query.id() != null) builder.and(settlement.id.eq(query.id()));
        if (!CollectionUtils.isEmpty(query.ids())) builder.and(settlement.id.in(query.ids()));
        if (query.startDate() != null) builder.and(settlement.startDate.eq(query.startDate()));
        if (query.startDateFrom() != null) builder.and(settlement.startDate.goe(query.startDateFrom()));
        if (query.startDateTo() != null) builder.and(settlement.startDate.loe(query.startDateTo()));
        if (query.endDate() != null) builder.and(settlement.endDate.eq(query.endDate()));
        if (query.endDateFrom() != null) builder.and(settlement.endDate.goe(query.endDateFrom()));
        if (query.endDateTo() != null) builder.and(settlement.endDate.loe(query.endDateTo()));
        if (query.amount() != null) builder.and(settlement.amount.eq(query.amount()));
        if (query.amountMin() != null) builder.and(settlement.amount.goe(query.amountMin()));
        if (query.amountMax() != null) builder.and(settlement.amount.loe(query.amountMax()));
        if (query.settlementStatus() != null) builder.and(settlement.settlement_status.eq(query.settlementStatus()));
        if (!CollectionUtils.isEmpty(query.settlementStatuses())) {
            builder.and(settlement.settlement_status.in(query.settlementStatuses()));
        }
        if (query.createdAt() != null) builder.and(settlement.inst_dt.eq(query.createdAt()));
        if (query.updatedAt() != null) builder.and(settlement.updt_dt.eq(query.updatedAt()));
        if (query.deletedAt() != null) builder.and(settlement.deleted_at.eq(query.deletedAt()));
        if (query.createdStartAt() != null) builder.and(settlement.inst_dt.goe(query.createdStartAt()));
        if (query.createdEndAt() != null) builder.and(settlement.inst_dt.loe(query.createdEndAt()));
        if (query.updatedStartAt() != null) builder.and(settlement.updt_dt.goe(query.updatedStartAt()));
        if (query.updatedEndAt() != null) builder.and(settlement.updt_dt.loe(query.updatedEndAt()));
        if (query.deletedStartAt() != null) builder.and(settlement.deleted_at.goe(query.deletedStartAt()));
        if (query.deletedEndAt() != null) builder.and(settlement.deleted_at.loe(query.deletedEndAt()));
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
        if (!hasIdOrder) orders.add(settlement.id.desc());
        return orders.toArray(OrderSpecifier[]::new);
    }

    private ComparableExpressionBase<?> sortExpression(String property) {
        return switch (property) {
            case "id" -> settlement.id;
            case "startDate" -> settlement.startDate;
            case "endDate" -> settlement.endDate;
            case "amount" -> settlement.amount;
            case "settlementStatus" -> settlement.settlement_status;
            case "createdAt" -> settlement.inst_dt;
            case "updatedAt" -> settlement.updt_dt;
            case "deletedAt" -> settlement.deleted_at;
            default -> null;
        };
    }

    private OrderSpecifier<?>[] defaultOrder() {
        return new OrderSpecifier<?>[] {settlement.id.desc()};
    }
}
