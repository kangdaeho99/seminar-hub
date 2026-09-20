package com.seminarhub.domain.settlement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SettlementItemSearchQuery(
        Long id,
        List<Long> ids,
        Long settlementId,
        List<Long> settlementIds,
        Long memberSeminarId,
        List<Long> memberSeminarIds,
        BigDecimal amount,
        BigDecimal amountMin,
        BigDecimal amountMax,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        LocalDateTime createdStartAt,
        LocalDateTime createdEndAt,
        LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt,
        LocalDateTime deletedStartAt,
        LocalDateTime deletedEndAt) {

    public boolean hasDeletedAtCondition() {
        return deletedAt != null || deletedStartAt != null || deletedEndAt != null;
    }
}
