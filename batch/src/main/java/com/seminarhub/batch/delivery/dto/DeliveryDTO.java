package com.seminarhub.batch.delivery.dto;

import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    
    private Long id;
    private Long memberSeminarId;
    private DeliveryStatus deliveryStatus;
    private String trackingNumber;
    private String courierCompany;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
