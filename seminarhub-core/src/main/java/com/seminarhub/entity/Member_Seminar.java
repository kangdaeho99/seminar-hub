package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member", "seminar"})
public class Member_Seminar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_seminar_no;

    /**
     * [ 2023-08-26 daeho.kang ]
     * Description :
     * 부모엔티티가 저장되면 자식 엔티티도 같이 저장되게 설정합니다.
     */
    @ManyToOne(
            targetEntity = Member.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(
            targetEntity = Seminar.class,
            fetch = FetchType.LAZY
            )
    @JoinColumn(name = "seminar_no")
    private Seminar seminar;

    @Column(nullable=true)
    private LocalDateTime del_dt;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }

}
