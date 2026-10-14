package com.seminarhub.batch.delivery.listener;

import com.seminarhub.domain.delivery.domain.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.SkipListener;

@Slf4j
public class DeliverySyncSkipListener implements SkipListener<Delivery, Delivery> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn(">>> [SKIP] Read 중 에러 발생: {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Delivery item, Throwable t) {
        log.warn(">>> [SKIP] Writer에서 오류 발생. Delivery ID: {}, Cause: {}",
                item != null ? item.getId() : "null", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Delivery item, Throwable t) {
        log.warn(">>> [SKIP] 동기 Processor에서 오류 발생. Delivery ID: {}, Cause: {}",
                item != null ? item.getId() : "null", t.getMessage());
    }
}
