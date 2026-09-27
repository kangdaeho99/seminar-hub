package com.seminarhub.api.settlement.dto;

import java.time.LocalDate;

public record SettlementDateUpdateRequest(
        Long memberSeminarItemId,
        LocalDate targetDate
) {
}
