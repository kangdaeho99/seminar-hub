package com.seminarhub.entity;

import com.seminarhub.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"memberSeminar"})
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSeminar memberSeminar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryStatus deliveryStatus;

    @Column(length = 100)
    private String trackingNumber; // 운송장 번호

    @Column(length = 50)
    private String courierCompany; // 택배사 (예: CJ대한통운, 우체국)

    public void updateStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }

    public void updateTrackingInfo(String courierCompany, String trackingNumber) {
        this.courierCompany = courierCompany;
        this.trackingNumber = trackingNumber;
    }
}
