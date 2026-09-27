package com.seminarhub.domain.seminar.domain;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("회원 세미나 참여(결제) 항목 정산 기준일")
@Table(indexes = @Index(name = "idx_mssd_msi_date_del", columnList = "member_seminar_item_id, date, deleted_at"))
public class MemberSeminarSettlementDate extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSeminarItem memberSeminarItem;

    @Column
    private LocalDate date;

    public MemberSeminarSettlementDate(MemberSeminarItem memberSeminarItem) {
        this.memberSeminarItem = memberSeminarItem;
    }

    public void update(MemberSeminarItem memberSeminarItem, LocalDate date) {
        if (memberSeminarItem != null) this.memberSeminarItem = memberSeminarItem;
        if (date != null) this.date = date;
        markUpdated();
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
