package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.seminarhub.enums.SettlementStatus;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private java.time.LocalDate startDate;

    @Column(nullable = false)
    private java.time.LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus settlement_status;

    @Column
    private java.time.LocalDateTime deleted_at;

    public void update(
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal amount,
            SettlementStatus settlementStatus) {
        if (startDate != null) this.startDate = startDate;
        if (endDate != null) this.endDate = endDate;
        if (amount != null) this.amount = amount;
        if (settlementStatus != null) this.settlement_status = settlementStatus;
    }

    public void delete() {
        this.deleted_at = LocalDateTime.now();
    }
}
