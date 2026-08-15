package com.seminarhub.listener;

import com.seminarhub.entity.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;

import java.util.concurrent.Future;

@Slf4j
public class DeliverySkipListener implements SkipListener<Delivery, Future<Delivery>> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn(">>> [SKIP] Read 중 에러 발생: {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Future<Delivery> item, Throwable t) {
        log.warn(">>> [SKIP] 비동기 처리 또는 Writer에서 오류 발생. Cause: {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Delivery item, Throwable t) {
        log.warn(">>> [SKIP] Processor 작업 제출 중 오류 발생. Delivery ID: {}, Cause: {}", item != null ? item.getId() : "null", t.getMessage());
    }
}
