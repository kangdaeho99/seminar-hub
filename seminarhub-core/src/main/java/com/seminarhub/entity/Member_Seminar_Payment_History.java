package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member_seminar"})
public class Member_Seminar_Payment_History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_seminar_payment_history_no;

    @OneToOne(mappedBy = "member_seminar_payment_history", fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seminar_no")
    private Member_Seminar member_seminar;

    @Column()
    private Long amount;

    @Column()
    private LocalDateTime del_dt;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }
    public void setMember_seminar(Member_Seminar member_seminar){
        this.member_seminar = member_seminar;
    }


}
