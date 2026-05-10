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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member_Seminar memberSeminar;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus settlement_status;

}
