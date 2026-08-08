package com.seminarhub.processor;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.enums.DeliveryStatus;
import com.seminarhub.util.DeliveryStatusExternalApiUtil;
import org.springframework.batch.item.ItemProcessor;

public class DeliveryItemProcessor implements ItemProcessor<Delivery, Delivery> {

    @Override
    public Delivery process(Delivery delivery) {
        DeliveryStatus externalStatus = DeliveryStatusExternalApiUtil.checkDeliveryStatus();
        delivery.updateStatus(externalStatus);
        return delivery;
    }
}
