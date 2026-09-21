package com.seminarhub.batch.delivery.reader;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.repository.DeliveryRepository;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DeliveryKeysetItemReader implements ItemReader<Delivery> {

    private static final long INITIAL_LAST_ID = 0L;

    private final DeliveryRepository deliveryRepository;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final Pageable pageRequest;

    private Iterator<Delivery> buffer = Collections.emptyIterator();
    private long lastId = INITIAL_LAST_ID;

    public DeliveryKeysetItemReader(
            DeliveryRepository deliveryRepository,
            LocalDateTime startAt,
            LocalDateTime endAt,
            int pageSize) {
        this.deliveryRepository = deliveryRepository;
        this.startAt = startAt;
        this.endAt = endAt;
        this.pageRequest = PageRequest.of(0, pageSize);
    }

    @Override
    public Delivery read() {
        if (!buffer.hasNext()) {
            List<Delivery> deliveries = deliveryRepository
                    .findByDeliveryStatusNotAndInstDtBetweenAndIdGreaterThan(
                            DeliveryStatus.DELIVERED,
                            startAt,
                            endAt,
                            lastId,
                            pageRequest);

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
