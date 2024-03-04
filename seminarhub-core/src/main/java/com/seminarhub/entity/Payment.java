package com.seminarhub.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long payment_no; //고유번호
    @Column(length = 100)
    private String member_id; //회원아이디

    @Column(nullable = false)
    private BigDecimal payment_amount; //회원 충전금액

    @Column()
    private LocalDateTime del_dt; //삭제일

}
