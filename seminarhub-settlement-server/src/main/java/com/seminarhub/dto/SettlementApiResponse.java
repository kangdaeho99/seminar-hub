package com.seminarhub.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementApiResponse {
    private boolean success;

    public static SettlementApiResponse ok() {
        return SettlementApiResponse.builder().success(true).build();
    }
}
