package com.seminarhub.domain.settlement.service;

import com.seminarhub.global.repository.AuditSearchQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SettlementItemSearchQuery(
        Long id, List<Long> ids,
        Long settlementId, List<Long> settlementIds,
        Long memberSeminarId, List<Long> memberSeminarIds,
        BigDecimal amount, BigDecimal amountMin, BigDecimal amountMax,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt,
        LocalDateTime updatedStartAt, LocalDateTime updatedEndAt,
        LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {

    public static SettlementItemSearchQuery empty() {
        return new SettlementItemSearchQuery(null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);
    }
}
