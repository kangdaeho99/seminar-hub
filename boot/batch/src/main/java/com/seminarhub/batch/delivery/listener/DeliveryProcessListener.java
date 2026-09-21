package com.seminarhub.batch.delivery.listener;

import com.seminarhub.domain.delivery.domain.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemProcessListener;

import java.util.concurrent.Future;

@Slf4j
public class DeliveryProcessListener implements ItemProcessListener<Delivery, Future<Delivery>> {
    
    @Override
    public void beforeProcess(Delivery item) {
        log.info(">>>> [Processor] 단건 가공 시작 - Delivery ID: {}", item.getId());
    }

    @Override
    public void afterProcess(Delivery item, Future<Delivery> result) {
        log.info("     [Processor] 비동기 작업 제출 완료 - Delivery ID: {}", item.getId());
    }
}
