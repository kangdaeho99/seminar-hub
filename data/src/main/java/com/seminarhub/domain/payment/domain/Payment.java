package com.seminarhub.domain.payment.domain;

import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Payment extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private BigDecimal paymentAmount;

    public void update(String email, BigDecimal paymentAmount) {
        if (email != null) this.email = email;
        if (paymentAmount != null) this.paymentAmount = paymentAmount;
        markUpdated();
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
