package com.seminarhub.processor;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.enums.DeliveryStatus;
import com.seminarhub.util.DeliveryStatusExternalApiUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

class DeliveryItemProcessorTest {

    private final DeliveryItemProcessor processor = new DeliveryItemProcessor();

    @Test
    void updatesDeliveryWithTheExternalStatusAndReturnsTheSameItem() throws Exception {
        Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        try (MockedStatic<DeliveryStatusExternalApiUtil> externalApi =
                     mockStatic(DeliveryStatusExternalApiUtil.class)) {
            externalApi.when(DeliveryStatusExternalApiUtil::checkDeliveryStatus)
                    .thenReturn(DeliveryStatus.SHIPPING);

            Delivery result = processor.process(delivery);

            assertThat(result).isSameAs(delivery);
            assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.SHIPPING);
            externalApi.verify(DeliveryStatusExternalApiUtil::checkDeliveryStatus);
        }
    }
}
