package com.seminarhub.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import com.seminarhub.enums.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/** 정산 수정 요청이다. 전달된 필드만 변경한다. */
@Schema(description = "정산 수정 요청")
public record SettlementUpdateRequest(
        @Schema(description = "변경할 정산 시작일", example = "2026-09-01", requiredMode = NOT_REQUIRED)
                LocalDate startDate,
        @Schema(description = "변경할 정산 종료일", example = "2026-09-30", requiredMode = NOT_REQUIRED)
                LocalDate endDate,
        @Positive(message = "정산 금액은 양수여야 합니다.")
                @Schema(description = "변경할 정산 금액", example = "150000", minimum = "0", exclusiveMinimum = true, requiredMode = NOT_REQUIRED)
                BigDecimal amount,
        @Schema(
                        description = "변경할 정산 상태",
                        implementation = SettlementStatus.class,
                        example = "COMPLETED",
                        requiredMode = NOT_REQUIRED)
                SettlementStatus settlementStatus) {

    @AssertTrue(message = "정산 시작일은 종료일보다 늦을 수 없습니다.")
    @Schema(hidden = true)
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
