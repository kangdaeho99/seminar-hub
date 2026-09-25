package com.seminarhub.batch.delivery.writer;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.service.DeliveryService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

@RequiredArgsConstructor
public class DeliveryItemWriter implements ItemWriter<Delivery> {
    private final DeliveryService deliveryService;

    @Override
    public void write(Chunk<? extends Delivery> items) {
        deliveryService.saveAll(new ArrayList<>(items.getItems()));
    }
}
