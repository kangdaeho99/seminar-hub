package com.seminarhub.dto;

import com.seminarhub.enums.SettlementStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SettlementSearchRequest(
        @Positive Long id,
        List<@Positive Long> ids,
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

    @AssertTrue(message = "startDateFrom은 startDateTo보다 늦을 수 없습니다.")
    public boolean isStartDateRangeValid() {
        return validRange(startDateFrom, startDateTo);
    }

    @AssertTrue(message = "endDateFrom은 endDateTo보다 늦을 수 없습니다.")
    public boolean isEndDateRangeValid() {
        return validRange(endDateFrom, endDateTo);
    }

    @AssertTrue(message = "amountMin은 amountMax보다 클 수 없습니다.")
    public boolean isAmountRangeValid() {
        return validRange(amountMin, amountMax);
    }

    @AssertTrue(message = "createdStartAt은 createdEndAt보다 늦을 수 없습니다.")
    public boolean isCreatedRangeValid() {
        return validRange(createdStartAt, createdEndAt);
    }

    @AssertTrue(message = "updatedStartAt은 updatedEndAt보다 늦을 수 없습니다.")
    public boolean isUpdatedRangeValid() {
        return validRange(updatedStartAt, updatedEndAt);
    }

    @AssertTrue(message = "deletedStartAt은 deletedEndAt보다 늦을 수 없습니다.")
    public boolean isDeletedRangeValid() {
        return validRange(deletedStartAt, deletedEndAt);
    }

    private static <T extends Comparable<? super T>> boolean validRange(T start, T end) {
        return start == null || end == null || start.compareTo(end) <= 0;
    }
}
