package com.seminarhub.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    PENDING("대기중"),
    SHIPPING("배송중"),
    DELIVERED("배송완료");

    private final String description;
}
