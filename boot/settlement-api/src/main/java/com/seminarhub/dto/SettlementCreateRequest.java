package com.seminarhub.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.seminarhub.enums.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/** 정산 생성 요청이다. */
@Schema(description = "정산 생성 요청")
public record SettlementCreateRequest(
        @NotNull(message = "정산 시작일은 필수입니다.")
                @Schema(description = "정산 시작일", example = "2026-09-01", requiredMode = REQUIRED)
                LocalDate startDate,
        @NotNull(message = "정산 종료일은 필수입니다.")
                @Schema(description = "정산 종료일", example = "2026-09-30", requiredMode = REQUIRED)
                LocalDate endDate,
        @NotNull(message = "정산 금액은 필수입니다.")
                @Positive(message = "정산 금액은 양수여야 합니다.")
                @Schema(description = "정산 금액", example = "150000", minimum = "0", exclusiveMinimum = true, requiredMode = REQUIRED)
                BigDecimal amount,
        @NotNull(message = "정산 상태는 필수입니다.")
                @Schema(
                        description = "정산 상태",
                        implementation = SettlementStatus.class,
                        example = "READY",
                        requiredMode = REQUIRED)
                SettlementStatus settlementStatus) {

    @AssertTrue(message = "정산 시작일은 종료일보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
