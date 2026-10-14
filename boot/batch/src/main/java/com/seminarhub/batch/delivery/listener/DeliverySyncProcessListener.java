package com.seminarhub.batch.delivery.listener;

import com.seminarhub.domain.delivery.domain.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemProcessListener;

@Slf4j
public class DeliverySyncProcessListener implements ItemProcessListener<Delivery, Delivery> {

    @Override
    public void beforeProcess(Delivery item) {
        log.info(">>>> [Processor] 단건 가공 시작 - Delivery ID: {}", item.getId());
    }

    @Override
    public void afterProcess(Delivery item, Delivery result) {
        log.info("     [Processor] 동기 처리 완료 - Delivery ID: {}", item.getId());
    }
}
