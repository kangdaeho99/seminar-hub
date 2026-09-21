package com.seminarhub.batch.delivery.util;

import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class DeliveryStatusExternalApiUtil {

    private DeliveryStatusExternalApiUtil() {}

    public static DeliveryStatus checkDeliveryStatus() {
        long delay = ThreadLocalRandom.current().nextLong(10, 50);
        
        try {
            log.info(">>>> 외부 배송 API 호출 대기 중... ({}ms)", delay);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DeliveryStatusUpdateException("API 연동 대기 중 인터럽트 발생", e);
        }

        double errorProbability = Math.random();
        if (errorProbability < 0.0000001) {
            throw new DeliveryStatusUpdateException("외부 배송 API 서버 연동 오류 발생");
        }

        double randomValue = Math.random();
        
        if (randomValue < 0.10) {
            return DeliveryStatus.PENDING;
        } else if (randomValue < 0.66) {
            return DeliveryStatus.SHIPPING;
        } else {
            return DeliveryStatus.DELIVERED;
        }
    }
}
