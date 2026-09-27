package com.seminarhub.domain.seminar.enums;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberSeminarStatus {
    ORDERED("미입금"),
    PAID("입금 확인"),
    PARTIAL_CANCELLED("부분 취소됨"),
    FULL_CANCELED("전체 취소됨");

    private final String description;

    public List<MemberSeminarStatus> trajectoryFromStart() {
        return switch (this) {
            case ORDERED -> List.of(ORDERED);
            case PAID -> List.of(ORDERED, PAID);
            case PARTIAL_CANCELLED -> List.of(ORDERED, PAID, PARTIAL_CANCELLED);
            case FULL_CANCELED -> List.of(ORDERED, PAID, FULL_CANCELED);
        };
    }
}
