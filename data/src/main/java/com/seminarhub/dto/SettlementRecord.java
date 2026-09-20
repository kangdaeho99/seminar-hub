package com.seminarhub.dto;

public record SettlementRecord(
        Long memberSeminarId,
        Long price,
        Long settlementDateId
) {}
