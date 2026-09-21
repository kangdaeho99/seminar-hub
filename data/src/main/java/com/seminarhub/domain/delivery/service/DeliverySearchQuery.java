package com.seminarhub.domain.delivery.service;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDateTime;
import java.util.List;
public record DeliverySearchQuery(Long id, List<Long> ids, Long memberSeminarId, List<Long> memberSeminarIds,
        DeliveryStatus deliveryStatus, List<DeliveryStatus> deliveryStatuses, String trackingNumber, String courierCompany,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static DeliverySearchQuery empty() { return new DeliverySearchQuery(null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null); }
}
