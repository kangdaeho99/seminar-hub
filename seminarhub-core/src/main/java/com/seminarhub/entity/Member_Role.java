package com.seminarhub.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
public class Member_Role extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_role_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member; //연관관계 지정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_no")
    private Role role;

    @Column(nullable=true)
    private LocalDateTime del_dt;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }

    public void setMember(Member member) {
        this.member = member;
    }


}
