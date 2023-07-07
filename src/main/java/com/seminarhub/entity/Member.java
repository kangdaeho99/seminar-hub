package com.seminarhub.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_no;

    @Column(length = 100, nullable = false, unique = true, name = "member_id")
    private String member_id;

    @Column(length = 100, nullable = false)
    private String member_password;

    @Column(length = 100, nullable = true)
    private String member_nickname;

    @Column(nullable = false)
    private boolean member_from_social;

    @Column(nullable=true)
    private LocalDateTime del_dt;

    public void setMember_nickname(String member_nickname){
        this.member_nickname = member_nickname;
    }

}
