package com.seminarhub.service;

import com.seminarhub.dto.DeliveryDTO;
import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.MemberSeminar;
import com.seminarhub.entity.enums.DeliveryStatus;
import com.seminarhub.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Long register(DeliveryDTO deliveryDTO) {
        Delivery delivery = dtoToEntity(deliveryDTO);
        delivery = deliveryRepository.save(delivery);
        return delivery.getId();
    }

    @Transactional(readOnly = true)
    public DeliveryDTO get(Long id) {
        Optional<Delivery> result = deliveryRepository.findByIdWithMemberSeminar(id);
        return result.map(this::entityToDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public DeliveryDTO getByMemberSeminarId(Long memberSeminarId) {
        Optional<Delivery> result = deliveryRepository.findByMemberSeminar_Id(memberSeminarId);
        return result.map(this::entityToDTO).orElse(null);
    }

    public void updateStatus(Long id, DeliveryStatus status) {
        Optional<Delivery> result = deliveryRepository.findById(id);
        result.ifPresent(delivery -> delivery.updateStatus(status));
    }

    public void updateTrackingInfo(Long id, String courierCompany, String trackingNumber) {
        Optional<Delivery> result = deliveryRepository.findById(id);
        result.ifPresent(delivery -> delivery.updateTrackingInfo(courierCompany, trackingNumber));
    }

    public Delivery dtoToEntity(DeliveryDTO dto) {
        return Delivery.builder()
                .id(dto.getId())
                .memberSeminar(MemberSeminar.builder().id(dto.getMemberSeminarId()).build())
                .deliveryStatus(dto.getDeliveryStatus() != null ? dto.getDeliveryStatus() : DeliveryStatus.PENDING)
                .trackingNumber(dto.getTrackingNumber())
                .courierCompany(dto.getCourierCompany())
                .build();
    }

    public DeliveryDTO entityToDTO(Delivery entity) {
        return DeliveryDTO.builder()
                .id(entity.getId())
                .memberSeminarId(entity.getMemberSeminar() != null ? entity.getMemberSeminar().getId() : null)
                .deliveryStatus(entity.getDeliveryStatus())
                .trackingNumber(entity.getTrackingNumber())
                .courierCompany(entity.getCourierCompany())
                .regDate(entity.getInst_dt())
                .modDate(entity.getUpdt_dt())
                .build();
    }
}
