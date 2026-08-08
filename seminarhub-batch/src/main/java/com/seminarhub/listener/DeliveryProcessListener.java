package com.seminarhub.listener;

import com.seminarhub.entity.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class DeliveryProcessListener implements ItemProcessListener<Delivery, Delivery> {
    
    @Override
    public void beforeProcess(Delivery item) {
        log.info(">>>> [Processor] 단건 가공 시작 - Delivery ID: {}", item.getId());
    }

    @Override
    public void afterProcess(Delivery item, Delivery result) {
        if (result == null) {
            log.info("     [Processor] 배송 미완료 (Writer 전달 제외) - Delivery ID: {}", item.getId());
        } else {
            log.info("     [Processor] 상태 업데이트 완료 - Delivery ID: {}", result.getId());
        }
    }
}
