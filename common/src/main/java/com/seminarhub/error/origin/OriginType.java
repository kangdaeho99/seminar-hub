package com.seminarhub.error.origin;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OriginType {
    COMMON("일반"),
    SEMINAR_HUB("Seminar Hub");

    private final String description;
}
