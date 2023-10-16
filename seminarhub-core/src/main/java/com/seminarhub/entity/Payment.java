package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member_seminar"})
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_no;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_no")
    private Member_Seminar member_seminar;

    @Column()
    private Long amount;

    @Column(nullable=true)
    private LocalDateTime del_dt;


    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }
    public void setMember_seminar(Member_Seminar member_seminar){
        this.member_seminar = member_seminar;
    }

}
