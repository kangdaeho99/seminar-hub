package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

import com.seminarhub.enums.SettlementStatus;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberSeminar")
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlement_no;

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
}
