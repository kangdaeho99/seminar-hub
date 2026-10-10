package com.seminarhub.statemachine.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransitionRejectionCode {

    PAYMENT_NOT_CONFIRMED(
            "결제 성공 내역이 없어 입금을 확인할 수 없습니다."
    ),

    CANCEL_ORDER_STATE_MISMATCH(
            "전체 취소는 취소되지 않은 모든 항목이 결제 완료 상태일 때 가능합니다."
    ),

    CANCEL_ITEMS_INCOMPLETE(
            "전체 취소 대상 항목이 요청에 모두 포함되지 않았습니다."
    ),

    TRANSITION_NOT_ALLOWED(
            "현재 상태에서는 요청한 작업을 처리할 수 없습니다."
    );

    private final String message;
}
