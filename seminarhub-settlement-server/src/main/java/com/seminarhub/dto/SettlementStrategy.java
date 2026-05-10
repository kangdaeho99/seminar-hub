package com.seminarhub.dto;

public enum SettlementStrategy {
    PESSIMISTIC_WRITE_READ,
    READ_COMMITTED,
    REPEATABLE_READ,
    SERIALIZABLE
}
