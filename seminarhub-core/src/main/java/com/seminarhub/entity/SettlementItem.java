package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SettlementItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlement_item_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Settlement settlement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member_Seminar memberSeminar;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private java.time.LocalDateTime deleted_at;
}
