package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("회원 세미나 참여(결제) 항목 정산 기준일")
public class MemberSeminarSettlementDate extends BaseEntity { // (선택) 등록/수정일이 필요하다면 BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seminar_settlement_date_no", updatable = false)
    @Comment("pk")
    private Long id;

    // FetchType.LAZY를 명시하여 지연 로딩 설정 (N+1 문제 방지 및 트랜잭션 테스트 용이)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member_Seminar memberSeminar;

    @Column
    @Comment("정산 기준일, 만약 null이면 정산일 세팅전입니다.")
    private LocalDate date;

    // 생성자
    public MemberSeminarSettlementDate(Member_Seminar memberSeminar) {
        this.memberSeminar = memberSeminar;
    }

    // 비즈니스 로직: 정산일 업데이트
    public void updateDate(LocalDate date) {
        this.date = date;
    }
}
