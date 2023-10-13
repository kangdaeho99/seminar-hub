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

    @OneToOne
    private Payment payment;

    @Column(nullable=true)
    private LocalDateTime del_dt;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }

}
