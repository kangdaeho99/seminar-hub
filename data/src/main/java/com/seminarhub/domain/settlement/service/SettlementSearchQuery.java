package com.seminarhub.domain.settlement.service;

import com.seminarhub.enums.SettlementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SettlementSearchQuery(
        Long id,
        List<Long> ids,
        LocalDate startDate,
        LocalDate startDateFrom,
        LocalDate startDateTo,
        LocalDate endDate,
        LocalDate endDateFrom,
        LocalDate endDateTo,
        BigDecimal amount,
        BigDecimal amountMin,
        BigDecimal amountMax,
        SettlementStatus settlementStatus,
        List<SettlementStatus> settlementStatuses,
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
