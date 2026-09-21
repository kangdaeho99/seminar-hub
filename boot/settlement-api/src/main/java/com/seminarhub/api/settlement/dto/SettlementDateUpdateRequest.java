package com.seminarhub.api.settlement.dto;

import java.time.LocalDate;

public record SettlementDateUpdateRequest(
        Long memberSeminarId,
        LocalDate targetDate
) {
}
