package com.seminarhub.api.settlement.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.enums.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "정산 응답")
public record SettlementResponse(
        @Schema(description = "정산 ID", example = "1024", requiredMode = NOT_REQUIRED) Long id,
        @Schema(description = "정산 시작일", example = "2026-09-01", requiredMode = NOT_REQUIRED) LocalDate startDate,
        @Schema(description = "정산 종료일", example = "2026-09-30", requiredMode = NOT_REQUIRED) LocalDate endDate,
        @Schema(description = "정산 금액", example = "150000", requiredMode = NOT_REQUIRED) BigDecimal amount,
        @Schema(
                        description = "정산 상태",
                        implementation = SettlementStatus.class,
                        example = "COMPLETED",
                        requiredMode = NOT_REQUIRED)
                SettlementStatus settlementStatus,
        @Schema(description = "생성 일시", example = "2026-09-30T10:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime createdAt,
        @Schema(description = "수정 일시", example = "2026-09-30T11:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime updatedAt,
        @Schema(description = "삭제 일시", example = "2026-09-30T12:00:00", requiredMode = NOT_REQUIRED)
                LocalDateTime deletedAt) {

    public static SettlementResponse from(Settlement settlement) {
        return new SettlementResponse(
                settlement.getId(), settlement.getStartDate(), settlement.getEndDate(), settlement.getAmount(),
                settlement.getSettlementStatus(), settlement.getCreatedAt(), settlement.getUpdatedAt(),
                settlement.getDeletedAt());
    }
}
