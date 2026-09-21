package com.seminarhub.batch.delivery.listener;

import com.seminarhub.domain.delivery.domain.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemReadListener;

@Slf4j
public class DeliveryReadListener implements ItemReadListener<Delivery> {

    @Override
    public void beforeRead() {
        log.info(">> [Reader] 단건 읽기 시작");
    }
    
    @Override
    public void afterRead(Delivery item) {
        log.info(">> [Reader] 단건 읽기 완료 - Delivery ID: {}", item.getId());
    }
}
