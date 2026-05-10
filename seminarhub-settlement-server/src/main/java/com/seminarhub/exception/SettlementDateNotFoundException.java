package com.seminarhub.exception;

public class SettlementDateNotFoundException extends RuntimeException {
    public SettlementDateNotFoundException(Long settlementDateId) {
        super("MemberSeminarSettlementDate not found. settlementDateId=" + settlementDateId);
    }
}
