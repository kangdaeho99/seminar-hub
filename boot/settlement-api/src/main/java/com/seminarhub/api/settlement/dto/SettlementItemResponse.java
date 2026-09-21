package com.seminarhub.api.settlement.dto;

import com.seminarhub.domain.settlement.domain.SettlementItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SettlementItemResponse(
        Long id,
        Long settlementId,
        Long memberSeminarId,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {

    public static SettlementItemResponse from(SettlementItem item) {
        return new SettlementItemResponse(
                item.getId(), item.getSettlement().getId(), item.getMemberSeminar().getId(), item.getAmount(),
                item.getCreatedAt(), item.getUpdatedAt(), item.getDeletedAt());
    }
}
