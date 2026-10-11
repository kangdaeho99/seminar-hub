package com.seminarhub.domain.seminar.domain;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "member_seminar_items_status_history",
        indexes = {
                @Index(
                        name = "idx_item_status_history_item_id",
                        columnList = "member_seminar_item_id, id"
                )
        }
)
public class MemberSeminarItemStatusHistory extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "member_seminar_item_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private MemberSeminarItem memberSeminarItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, updatable = false, length = 30)
    private MemberSeminarItemStatus toStatus;

    public MemberSeminarItemStatusHistory(
            MemberSeminarItem memberSeminarItem,
            MemberSeminarItemStatus toStatus
    ) {
        this.memberSeminarItem = memberSeminarItem;
        this.toStatus = toStatus;
    }
}
