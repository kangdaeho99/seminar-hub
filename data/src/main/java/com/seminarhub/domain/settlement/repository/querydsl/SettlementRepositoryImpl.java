package com.seminarhub.domain.settlement.repository.querydsl;

import static com.seminarhub.domain.settlement.domain.QSettlement.settlement;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class SettlementRepositoryImpl extends QuerydslRepositorySupport<Settlement> implements SettlementRepositoryCustom {
    public SettlementRepositoryImpl(JPAQueryFactory factory) {
        super(factory, settlement, settlement.id, settlement.createdAt, settlement.updatedAt, settlement.deletedAt);
    }

    public List<Settlement> search(SettlementSearchQuery q) { return findAll(condition(q)); }
    public Page<Settlement> search(SettlementSearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<Settlement> search(SettlementSearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }

    private BooleanBuilder condition(SettlementSearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (q.startDate() != null) b.and(settlement.startDate.eq(q.startDate()));
        if (q.startDateFrom() != null) b.and(settlement.startDate.goe(q.startDateFrom()));
        if (q.startDateTo() != null) b.and(settlement.startDate.loe(q.startDateTo()));
        if (q.endDate() != null) b.and(settlement.endDate.eq(q.endDate()));
        if (q.endDateFrom() != null) b.and(settlement.endDate.goe(q.endDateFrom()));
        if (q.endDateTo() != null) b.and(settlement.endDate.loe(q.endDateTo()));
        if (q.amount() != null) b.and(settlement.amount.eq(q.amount()));
        if (q.amountMin() != null) b.and(settlement.amount.goe(q.amountMin()));
        if (q.amountMax() != null) b.and(settlement.amount.loe(q.amountMax()));
        if (q.settlementStatus() != null) b.and(settlement.settlementStatus.eq(q.settlementStatus()));
        if (!CollectionUtils.isEmpty(q.settlementStatuses())) b.and(settlement.settlementStatus.in(q.settlementStatuses()));
        return b;
    }

    private ComparableExpressionBase<?> sort(String name) {
        return switch (name) {
            case "id" -> settlement.id;
            case "startDate" -> settlement.startDate;
            case "endDate" -> settlement.endDate;
            case "amount" -> settlement.amount;
            case "settlementStatus" -> settlement.settlementStatus;
            case "createdAt" -> settlement.createdAt;
            case "updatedAt" -> settlement.updatedAt;
            case "deletedAt" -> settlement.deletedAt;
            default -> null;
        };
    }
}
