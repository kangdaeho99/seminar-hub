package com.seminarhub.domain.settlement.repository.querydsl;

import static com.seminarhub.domain.settlement.domain.QSettlementItem.settlementItem;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.settlement.domain.SettlementItem;
import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class SettlementItemRepositoryImpl extends QuerydslRepositorySupport<SettlementItem> implements SettlementItemRepositoryCustom {
    public SettlementItemRepositoryImpl(JPAQueryFactory factory) {
        super(factory, settlementItem, settlementItem.id, settlementItem.createdAt, settlementItem.updatedAt, settlementItem.deletedAt);
    }

    public List<SettlementItem> search(SettlementItemSearchQuery q) { return findAll(condition(q)); }
    public Page<SettlementItem> search(SettlementItemSearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<SettlementItem> search(SettlementItemSearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }

    private BooleanBuilder condition(SettlementItemSearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (q.settlementId() != null) b.and(settlementItem.settlement.id.eq(q.settlementId()));
        if (!CollectionUtils.isEmpty(q.settlementIds())) b.and(settlementItem.settlement.id.in(q.settlementIds()));
        if (q.memberSeminarId() != null) b.and(settlementItem.memberSeminar.id.eq(q.memberSeminarId()));
        if (!CollectionUtils.isEmpty(q.memberSeminarIds())) b.and(settlementItem.memberSeminar.id.in(q.memberSeminarIds()));
        if (q.amount() != null) b.and(settlementItem.amount.eq(q.amount()));
        if (q.amountMin() != null) b.and(settlementItem.amount.goe(q.amountMin()));
        if (q.amountMax() != null) b.and(settlementItem.amount.loe(q.amountMax()));
        return b;
    }

    private ComparableExpressionBase<?> sort(String name) {
        return switch (name) {
            case "id" -> settlementItem.id;
            case "settlementId" -> settlementItem.settlement.id;
            case "memberSeminarId" -> settlementItem.memberSeminar.id;
            case "amount" -> settlementItem.amount;
            case "createdAt" -> settlementItem.createdAt;
            case "updatedAt" -> settlementItem.updatedAt;
            case "deletedAt" -> settlementItem.deletedAt;
            default -> null;
        };
    }
}
