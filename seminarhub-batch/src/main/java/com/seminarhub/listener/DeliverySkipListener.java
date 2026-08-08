package com.seminarhub.listener;

import com.seminarhub.entity.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;

@Slf4j
public class DeliverySkipListener implements SkipListener<Delivery, Delivery> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn(">>> [SKIP] Read 중 에러 발생: {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Delivery item, Throwable t) {
        log.warn(">>> [SKIP] Writer에서 에러 발생. Delivery ID: {}, Cause: {}", item != null ? item.getId() : "null", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Delivery item, Throwable t) {
        log.warn(">>> [SKIP] Processor에서 API 에러로 인해 다음 배송 건 스킵됨. Delivery ID: {}, Cause: {}", item != null ? item.getId() : "null", t.getMessage());
    }
}
