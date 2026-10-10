package com.seminarhub.domain.payment.domain;

import com.seminarhub.domain.payment.enums.PaymentStatus;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(indexes = @Index(name = "idx_payment_item_status",
        columnList = "member_seminar_item_id,payment_status,deleted_at"))
public class Payment extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private BigDecimal paymentAmount;

    // Existing payments may not yet be associated with an item.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_item_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSeminarItem memberSeminarItem;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    @ColumnDefault("'PENDING'")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    public void changeStatus(PaymentStatus status) {
        this.paymentStatus = status;
        markUpdated();
    }

    public void update(String email, BigDecimal paymentAmount) {
        if (email != null) this.email = email;
        if (paymentAmount != null) this.paymentAmount = paymentAmount;
        markUpdated();
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
