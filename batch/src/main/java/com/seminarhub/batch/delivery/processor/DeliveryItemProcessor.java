package com.seminarhub.batch.delivery.processor;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.batch.delivery.util.DeliveryStatusExternalApiUtil;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class DeliveryItemProcessor implements ItemProcessor<Delivery, Delivery> {

    @Override
    public Delivery process(Delivery delivery) {
        DeliveryStatus externalStatus = DeliveryStatusExternalApiUtil.checkDeliveryStatus();
        delivery.updateStatus(externalStatus);
        return delivery;
    }
}
