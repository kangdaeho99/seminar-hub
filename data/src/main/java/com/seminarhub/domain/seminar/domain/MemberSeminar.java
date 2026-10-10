package com.seminarhub.domain.seminar.domain;

import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.seminar.enums.MemberSeminarStatus;
import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@ToString
public class MemberSeminar extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberSeminarStatus orderStatus = MemberSeminarStatus.ORDERED;

    public void update(Member member) {
        if (member != null) this.member = member;
        markUpdated();
    }

    public void changeStatus(MemberSeminarStatus status) {
        this.orderStatus = Objects.requireNonNull(status, "status");
        markUpdated();
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
