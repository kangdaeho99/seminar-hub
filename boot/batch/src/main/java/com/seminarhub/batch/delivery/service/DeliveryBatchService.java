package com.seminarhub.batch.delivery.service;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.service.DeliveryService;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.batch.delivery.dto.DeliveryDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class DeliveryBatchService {
    private final DeliveryService deliveryService;

    public Long register(DeliveryDTO dto) {
        Delivery delivery = deliveryService.save(dtoToEntity(dto));
        return delivery.getId();
    }

    @Transactional(readOnly = true)
    public DeliveryDTO get(Long id) {
        return deliveryService.findByIdWithMemberSeminarItem(id).map(this::entityToDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public DeliveryDTO getByMemberSeminarItemId(Long id) {
        return deliveryService.findByMemberSeminarItemId(id).map(this::entityToDTO).orElse(null);
    }

    public void updateStatus(Long id, DeliveryStatus status) {
        deliveryService.updateStatusById(id, status);
    }

    public void updateTrackingInfo(Long id, String courier, String tracking) {
        deliveryService.updateTrackingInfoById(id, courier, tracking);
    }

    public Delivery dtoToEntity(DeliveryDTO dto) {
        return Delivery.builder().id(dto.getId()).memberSeminarItem(MemberSeminarItem.builder().id(dto.getMemberSeminarItemId()).build())
                .deliveryStatus(Optional.ofNullable(dto.getDeliveryStatus()).orElse(DeliveryStatus.PENDING))
                .trackingNumber(dto.getTrackingNumber()).courierCompany(dto.getCourierCompany()).build();
    }

    public DeliveryDTO entityToDTO(Delivery entity) {
        return DeliveryDTO.builder().id(entity.getId())
                .memberSeminarItemId(entity.getMemberSeminarItem() == null ? null : entity.getMemberSeminarItem().getId())
                .deliveryStatus(entity.getDeliveryStatus()).trackingNumber(entity.getTrackingNumber())
                .courierCompany(entity.getCourierCompany()).regDate(entity.getCreatedAt()).modDate(entity.getUpdatedAt()).build();
    }
}
