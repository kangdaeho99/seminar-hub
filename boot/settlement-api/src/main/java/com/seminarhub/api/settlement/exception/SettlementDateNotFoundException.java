package com.seminarhub.api.settlement.exception;

import com.seminarhub.error.NotFoundException;
import com.seminarhub.error.origin.ErrorOrigin.SeminarHubError;

public class SettlementDateNotFoundException extends NotFoundException {
    public SettlementDateNotFoundException(Long settlementDateId) {
        super(
                SeminarHubError.SETTLEMENT_DATE_NOT_FOUND,
                "MemberSeminarSettlementDate not found. settlementDateId=" + settlementDateId);
    }
}
