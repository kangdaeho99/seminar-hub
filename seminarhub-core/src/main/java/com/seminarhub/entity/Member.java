package com.seminarhub.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
@ToString(exclude = {"member_role_set", "member_seminar_list"})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //고유번호

    @Column(length = 500)
    private String email; //회원아이디

    @Column(length = 500)
    private String password; //회원 비밀번호

    @Column(length = 500)
    private String nickname; //회원닉네임

    @Column
    @ColumnDefault("false")
    private boolean from_social;

    @Column(precision = 19, scale = 2)
    @ColumnDefault("0.0")
    private BigDecimal charged_money; //회원 충전금액

    @Column()
    private LocalDateTime del_dt; //삭제일

    @OneToMany(mappedBy = "member",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<Member_Role> member_role_set = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Member_Seminar> member_seminar_list;

    public Member(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDel_dt(LocalDateTime del_dt) {
        this.del_dt = del_dt;
    }

    public void addMemberRole(Member_Role member_role) {
        member_role_set.add(member_role);
    }

}
