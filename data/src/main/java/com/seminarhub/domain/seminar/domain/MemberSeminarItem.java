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
import java.time.LocalDateTime;
import java.util.Objects;
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
@ToString(exclude = {"memberSeminar", "seminar"})
@Table(indexes = {
        @Index(name = "idx_member_seminar_item_order", columnList = "member_seminar_id"),
        @Index(name = "idx_member_seminar_item_seminar", columnList = "seminar_id")
})
public class MemberSeminarItem extends AuditMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_seminar_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSeminar memberSeminar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seminar_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Seminar seminar;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "member_seminar_item_status", nullable = false, length = 30)
    private MemberSeminarItemStatus memberSeminarItemStatus = MemberSeminarItemStatus.ORDERED;

    public void update(MemberSeminar memberSeminar, Seminar seminar) {
        if (memberSeminar != null) this.memberSeminar = memberSeminar;
        if (seminar != null) this.seminar = seminar;
        markUpdated();
    }

    public void changeStatus(MemberSeminarItemStatus status) {
        this.memberSeminarItemStatus = Objects.requireNonNull(status, "status");
        markUpdated();
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
