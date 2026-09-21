package com.seminarhub.domain.delivery.domain;

import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberSeminar")
public class Delivery extends AuditMetadata {

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
    private String trackingNumber;

    @Column(length = 50)
    private String courierCompany;

    public void update(MemberSeminar memberSeminar, DeliveryStatus status, String trackingNumber, String courierCompany) {
        if (memberSeminar != null) this.memberSeminar = memberSeminar;
        if (status != null) this.deliveryStatus = status;
        if (trackingNumber != null) this.trackingNumber = trackingNumber;
        if (courierCompany != null) this.courierCompany = courierCompany;
        markUpdated();
    }

    public void updateStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }

    public void updateTrackingInfo(String courierCompany, String trackingNumber) {
        this.courierCompany = courierCompany;
        this.trackingNumber = trackingNumber;
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
