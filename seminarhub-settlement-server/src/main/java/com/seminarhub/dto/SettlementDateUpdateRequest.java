package com.seminarhub.dto;

import java.time.LocalDate;

public record SettlementDateUpdateRequest(
        Long settlementDateId,
        LocalDate targetDate
) {
}
