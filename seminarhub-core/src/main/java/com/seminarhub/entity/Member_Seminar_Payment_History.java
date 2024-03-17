package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member_Seminar_Payment_History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_seminar_payment_history_no;

    @Column(nullable = false)
    private Long member_seminar_payment_history_amount;

//    JPA에서 제공하지 않음.
//    @OneToOne(mappedBy = "member_seminar_payment_history")
//    private Member_Seminar member_seminar;

    @Column()
    private LocalDateTime del_dt;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }

}
