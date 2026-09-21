package com.seminarhub.domain.settlement.domain;

import com.seminarhub.domain.settlement.enums.SettlementStatus;
import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@ToString
public class Settlement extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus settlementStatus;

    public void update(LocalDate startDate, LocalDate endDate, BigDecimal amount, SettlementStatus settlementStatus) {
        if (startDate != null) this.startDate = startDate;
        if (endDate != null) this.endDate = endDate;
        if (amount != null) this.amount = amount;
        if (settlementStatus != null) this.settlementStatus = settlementStatus;
        markUpdated();
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
