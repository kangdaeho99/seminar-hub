package com.seminarhub.error.origin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public abstract class ErrorOrigin {

    private ErrorOrigin() {}

    @Getter
    @RequiredArgsConstructor
    public enum CommonError implements Origin {
        BAD_REQUEST(HttpStatus.BAD_REQUEST),
        CONFLICT(HttpStatus.CONFLICT),
        NOT_FOUND(HttpStatus.NOT_FOUND),
        INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

        private final int code;
        private final String message;

        CommonError(HttpStatus httpStatus) {
            this.code = httpStatus.value();
            this.message = httpStatus.getReasonPhrase();
        }

        @Override
        public OriginType getOrigin() {
            return OriginType.COMMON;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum SeminarHubError implements Origin {
        SETTLEMENT_DATE_NOT_FOUND(1000, "정산일 정보를 찾을 수 없습니다."),
        TRANSACTION_INTERRUPTED(1001, "트랜잭션 처리 중 인터럽트가 발생했습니다."),
        DELIVERY_STATUS_UPDATE_FAILED(1002, "배송 상태 업데이트에 실패했습니다.");

        private final int code;
        private final String message;

        @Override
        public OriginType getOrigin() {
            return OriginType.SEMINAR_HUB;
        }
    }
}
