package com.seminarhub.dto;

import com.seminarhub.entity.Settlement;
import com.seminarhub.enums.SettlementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SettlementResponse(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal amount,
        SettlementStatus settlementStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {

    public static SettlementResponse from(Settlement settlement) {
        return new SettlementResponse(
                settlement.getId(), settlement.getStartDate(), settlement.getEndDate(), settlement.getAmount(),
                settlement.getSettlement_status(), settlement.getInst_dt(), settlement.getUpdt_dt(),
                settlement.getDeleted_at());
    }
}
