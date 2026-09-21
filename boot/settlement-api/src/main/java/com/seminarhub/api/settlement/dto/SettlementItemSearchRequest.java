package com.seminarhub.api.settlement.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SettlementItemSearchRequest(
        @Positive Long id,
        List<@Positive Long> ids,
        @Positive Long settlementId,
        List<@Positive Long> settlementIds,
        @Positive Long memberSeminarId,
        List<@Positive Long> memberSeminarIds,
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

    @AssertTrue(message = "amountMin은 amountMax보다 클 수 없습니다.")
    public boolean isAmountRangeValid() { return validRange(amountMin, amountMax); }

    @AssertTrue(message = "createdStartAt은 createdEndAt보다 늦을 수 없습니다.")
    public boolean isCreatedRangeValid() { return validRange(createdStartAt, createdEndAt); }

    @AssertTrue(message = "updatedStartAt은 updatedEndAt보다 늦을 수 없습니다.")
    public boolean isUpdatedRangeValid() { return validRange(updatedStartAt, updatedEndAt); }

    @AssertTrue(message = "deletedStartAt은 deletedEndAt보다 늦을 수 없습니다.")
    public boolean isDeletedRangeValid() { return validRange(deletedStartAt, deletedEndAt); }

    private static <T extends Comparable<? super T>> boolean validRange(T start, T end) {
        return start == null || end == null || start.compareTo(end) <= 0;
    }
}
