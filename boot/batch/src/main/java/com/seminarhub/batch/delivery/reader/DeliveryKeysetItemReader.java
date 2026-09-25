package com.seminarhub.batch.delivery.reader;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemReader;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class DeliveryKeysetItemReader implements ItemReader<Delivery> {

    private static final long INITIAL_LAST_ID = 0L;

    private final DeliveryService deliveryService;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final int pageSize;

    private Iterator<Delivery> buffer = Collections.emptyIterator();
    private long lastId = INITIAL_LAST_ID;

    @Override
    public Delivery read() {
        if (!buffer.hasNext()) {
            List<Delivery> deliveries = deliveryService.findNextBatch(startAt, endAt, lastId, pageSize);

            if (deliveries.isEmpty()) {
                return null;
            }

            buffer = deliveries.iterator();
        }

        Delivery delivery = buffer.next();
        lastId = delivery.getId();
        return delivery;
    }
}
