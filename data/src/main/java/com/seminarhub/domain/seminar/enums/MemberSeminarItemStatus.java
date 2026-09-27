package com.seminarhub.domain.seminar.enums;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberSeminarItemStatus {
    ORDERED("미입금"),
    PAID("입금 확인"),
    PACKING("배송 준비"),
    SCHEDULED("배송 예약"),
    IN_TRANSIT("배송 중"),
    DELIVERED("배송 완료"),
    RETURNING("반품 중"),
    RETURNED("반품 완료"),
    EXCHANGING("교환 중"),
    EXCHANGED("교환 완료"),
    CANCELLED("주문 취소"),
    TRASH("휴지통");

    private final String description;

    private static final List<MemberSeminarItemStatus> NOT_PURCHASE_CONFIRMED =
            List.of(ORDERED, TRASH, RETURNING, RETURNED, CANCELLED);
    private static final List<MemberSeminarItemStatus> PURCHASE_CONFIRMED = Arrays.stream(values())
            .filter(status -> !NOT_PURCHASE_CONFIRMED.contains(status)).toList();
    private static final List<String> PURCHASE_CONFIRMED_NAME_LIST =
            PURCHASE_CONFIRMED.stream().map(MemberSeminarItemStatus::name).toList();
    private static final List<MemberSeminarItemStatus> REVIEWABLE_STATUS_LIST =
            List.of(DELIVERED, EXCHANGING, EXCHANGED);

    public static List<MemberSeminarItemStatus> getPurchaseConfirmed() { return PURCHASE_CONFIRMED; }
    public static List<MemberSeminarItemStatus> getReviewableStatusList() { return REVIEWABLE_STATUS_LIST; }
    public static List<String> getPurchaseConfirmedNameList() { return PURCHASE_CONFIRMED_NAME_LIST; }
    public boolean isCancelled() { return CANCELLED.equals(this); }
    public boolean isReturnOrExchange() {
        return this == RETURNING || this == RETURNED || this == EXCHANGING || this == EXCHANGED;
    }
    public boolean isDelivered() { return DELIVERED.equals(this); }

    public List<MemberSeminarItemStatus> trajectoryFromStart() {
        return switch (this) {
            case ORDERED -> List.of(ORDERED);
            case PAID -> List.of(ORDERED, PAID);
            case PACKING -> List.of(ORDERED, PAID, PACKING);
            case SCHEDULED -> List.of(ORDERED, PAID, PACKING, SCHEDULED);
            case IN_TRANSIT -> List.of(ORDERED, PAID, PACKING, IN_TRANSIT);
            case DELIVERED -> List.of(ORDERED, PAID, PACKING, IN_TRANSIT, DELIVERED);
            case RETURNING -> List.of(ORDERED, PAID, PACKING, IN_TRANSIT, DELIVERED, RETURNING);
            case RETURNED -> List.of(ORDERED, PAID, PACKING, IN_TRANSIT, DELIVERED, RETURNING, RETURNED);
            case EXCHANGING -> List.of(ORDERED, PAID, PACKING, IN_TRANSIT, DELIVERED, EXCHANGING);
            case EXCHANGED -> List.of(ORDERED, PAID, PACKING, IN_TRANSIT, DELIVERED, EXCHANGING, EXCHANGED);
            case CANCELLED -> List.of(ORDERED, PAID, CANCELLED);
            case TRASH -> List.of(ORDERED, TRASH);
        };
    }
}
