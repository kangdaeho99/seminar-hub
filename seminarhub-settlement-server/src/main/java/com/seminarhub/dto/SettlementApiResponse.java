package com.seminarhub.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class SettlementApiResponse {
    private SettlementStrategy strategy;
    private SettlementOperation operation;
    private Long settlementDateId;
    private LocalDate startAt;
    private LocalDate endAt;
    private LocalDate targetDate;
    private LocalDate previousDate;
    private LocalDate resultDate;
    private Long totalSeminarPrice;
    private String threadName;
    private LocalDateTime transactionStartAt;
    private LocalDateTime transactionEndAt;
    private Long sleepMillis;
    private String isolationLevel;
    private String lockMode;
    private Integer lockedRowCount;
    private String message;
}
