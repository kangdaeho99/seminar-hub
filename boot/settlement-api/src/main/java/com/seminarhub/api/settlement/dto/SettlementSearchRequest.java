package com.seminarhub.api.settlement.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import com.seminarhub.domain.settlement.enums.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "정산 검색 조건")
public record SettlementSearchRequest(
        @Positive(message = "정산 ID는 양수여야 합니다.")
                @Schema(description = "[EQUAL] 정산 ID", example = "1024", requiredMode = NOT_REQUIRED)
                Long id,
        @Schema(description = "[IN] 정산 ID 목록", example = "1024,1025", requiredMode = NOT_REQUIRED)
                List<@Positive(message = "정산 ID는 양수여야 합니다.") Long> ids,
        @Schema(description = "[EQUAL] 정산 시작일", example = "2026-09-01", requiredMode = NOT_REQUIRED)
                LocalDate startDate,
        @Schema(description = "[RANGE: GTE] 정산 시작일 검색 시작값", example = "2026-09-01", requiredMode = NOT_REQUIRED)
                LocalDate startDateFrom,
        @Schema(description = "[RANGE: LTE] 정산 시작일 검색 종료값", example = "2026-09-30", requiredMode = NOT_REQUIRED)
                LocalDate startDateTo,
        @Schema(description = "[EQUAL] 정산 종료일", example = "2026-09-30", requiredMode = NOT_REQUIRED)
                LocalDate endDate,
        @Schema(description = "[RANGE: GTE] 정산 종료일 검색 시작값", example = "2026-09-01", requiredMode = NOT_REQUIRED)
                LocalDate endDateFrom,
        @Schema(description = "[RANGE: LTE] 정산 종료일 검색 종료값", example = "2026-09-30", requiredMode = NOT_REQUIRED)
                LocalDate endDateTo,
        @Positive(message = "정산 금액은 양수여야 합니다.")
                @Schema(description = "[EQUAL] 정산 금액", example = "150000", requiredMode = NOT_REQUIRED)
                BigDecimal amount,
        @Positive(message = "최소 정산 금액은 양수여야 합니다.")
                @Schema(description = "[RANGE: GTE] 최소 정산 금액", example = "100000", requiredMode = NOT_REQUIRED)
                BigDecimal amountMin,
        @Positive(message = "최대 정산 금액은 양수여야 합니다.")
                @Schema(description = "[RANGE: LTE] 최대 정산 금액", example = "200000", requiredMode = NOT_REQUIRED)
                BigDecimal amountMax,
        @Schema(description = "[EQUAL] 정산 상태", requiredMode = NOT_REQUIRED) SettlementStatus settlementStatus,
        @Schema(description = "[IN] 정산 상태 목록", requiredMode = NOT_REQUIRED)
                List<SettlementStatus> settlementStatuses,
        @Schema(description = "[EQUAL] 생성 일시", example = "2026-09-30T10:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime createdAt,
        @Schema(description = "[EQUAL] 수정 일시", example = "2026-09-30T11:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime updatedAt,
        @Schema(description = "[EQUAL] 삭제 일시", example = "2026-09-30T12:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime deletedAt,
        @Schema(description = "[RANGE: GTE] 생성 일시 검색 시작값", example = "2026-09-01T00:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime createdStartAt,
        @Schema(description = "[RANGE: LTE] 생성 일시 검색 종료값", example = "2026-09-30T23:59:59", requiredMode = NOT_REQUIRED)
                LocalDateTime createdEndAt,
        @Schema(description = "[RANGE: GTE] 수정 일시 검색 시작값", example = "2026-09-01T00:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime updatedStartAt,
        @Schema(description = "[RANGE: LTE] 수정 일시 검색 종료값", example = "2026-09-30T23:59:59", requiredMode = NOT_REQUIRED)
                LocalDateTime updatedEndAt,
        @Schema(description = "[RANGE: GTE] 삭제 일시 검색 시작값", example = "2026-09-01T00:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime deletedStartAt,
        @Schema(description = "[RANGE: LTE] 삭제 일시 검색 종료값", example = "2026-09-30T23:59:59", requiredMode = NOT_REQUIRED)
                LocalDateTime deletedEndAt) {

    @AssertTrue(message = "startDateFrom은 startDateTo보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isStartDateRangeValid() {
        return validRange(startDateFrom, startDateTo);
    }

    @AssertTrue(message = "endDateFrom은 endDateTo보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isEndDateRangeValid() {
        return validRange(endDateFrom, endDateTo);
    }

    @AssertTrue(message = "amountMin은 amountMax보다 클 수 없습니다.")
    @Schema(hidden = true)
    public boolean isAmountRangeValid() {
        return validRange(amountMin, amountMax);
    }

    @AssertTrue(message = "createdStartAt은 createdEndAt보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isCreatedRangeValid() {
        return validRange(createdStartAt, createdEndAt);
    }

    @AssertTrue(message = "updatedStartAt은 updatedEndAt보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isUpdatedRangeValid() {
        return validRange(updatedStartAt, updatedEndAt);
    }

    @AssertTrue(message = "deletedStartAt은 deletedEndAt보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isDeletedRangeValid() {
        return validRange(deletedStartAt, deletedEndAt);
    }

    private static <T extends Comparable<? super T>> boolean validRange(T start, T end) {
        return start == null || end == null || start.compareTo(end) <= 0;
    }
}
