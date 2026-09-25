package com.seminarhub.batch.delivery.service;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.service.DeliveryService;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
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
        return deliveryService.findByIdWithMemberSeminar(id).map(this::entityToDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public DeliveryDTO getByMemberSeminarId(Long id) {
        return deliveryService.findByMemberSeminarId(id).map(this::entityToDTO).orElse(null);
    }

    public void updateStatus(Long id, DeliveryStatus status) {
        deliveryService.updateStatusById(id, status);
    }

    public void updateTrackingInfo(Long id, String courier, String tracking) {
        deliveryService.updateTrackingInfoById(id, courier, tracking);
    }

    public Delivery dtoToEntity(DeliveryDTO dto) {
        return Delivery.builder().id(dto.getId()).memberSeminar(MemberSeminar.builder().id(dto.getMemberSeminarId()).build())
                .deliveryStatus(Optional.ofNullable(dto.getDeliveryStatus()).orElse(DeliveryStatus.PENDING))
                .trackingNumber(dto.getTrackingNumber()).courierCompany(dto.getCourierCompany()).build();
    }

    public DeliveryDTO entityToDTO(Delivery entity) {
        return DeliveryDTO.builder().id(entity.getId())
                .memberSeminarId(entity.getMemberSeminar() == null ? null : entity.getMemberSeminar().getId())
                .deliveryStatus(entity.getDeliveryStatus()).trackingNumber(entity.getTrackingNumber())
                .courierCompany(entity.getCourierCompany()).regDate(entity.getCreatedAt()).modDate(entity.getUpdatedAt()).build();
    }
}
