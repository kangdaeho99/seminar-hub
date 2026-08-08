package com.seminarhub.listener;

import com.seminarhub.entity.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

@Slf4j
public class DeliveryWriteListener implements ItemWriteListener<Delivery> {
    
    @Override
    public void beforeWrite(Chunk<? extends Delivery> items) {
        log.info(">>>>>> [Writer] 청크 단위 쓰기 시작 - 전달된 아이템 묶음 수: {}", items.size());
    }

    @Override
    public void afterWrite(Chunk<? extends Delivery> items) {
        log.info(">>>>>> [Writer] 청크 단위 쓰기 완료 - 쓰기 완료된 아이템 묶음 수: {}", items.size());
    }
}
