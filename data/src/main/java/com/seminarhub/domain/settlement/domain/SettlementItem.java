package com.seminarhub.domain.settlement.domain;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
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

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(indexes = @Index(name = "idx_settlement_item_msi_del", columnList = "member_seminar_item_id, deleted_at"))
public class SettlementItem extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Settlement settlement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSeminarItem memberSeminarItem;

    @Column(nullable = false)
    private BigDecimal amount;

    public void update(Settlement settlement, MemberSeminarItem memberSeminarItem, BigDecimal amount) {
        if (settlement != null) this.settlement = settlement;
        if (memberSeminarItem != null) this.memberSeminarItem = memberSeminarItem;
        if (amount != null) this.amount = amount;
        markUpdated();
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
