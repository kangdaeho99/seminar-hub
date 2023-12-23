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
@ToString(exclude = {"member_role_set", "member_seminar_list"})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long member_no; //고유번호

    @Column(length = 100, nullable = false, unique = true, name = "member_id")
    private String member_id; //회원아이디

    @Column(length = 100, nullable = false)
    private String member_password; //회원 비밀번호

    @Column(length = 100, nullable = false)
    private String member_nickname; //회원닉네임

    @Column(nullable = false)
    private boolean member_from_social; //회원 소셜로그인 여부

    @Column(nullable = false)
    private BigDecimal member_charged_money; //회원 충전금액

    @Column()
    private LocalDateTime del_dt; //삭제일

    @OneToMany(mappedBy = "member",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<Member_Role> member_role_set = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private List<Member_Seminar> member_seminar_list;

    public Member(long member_no) {
        this.member_no = member_no;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public void setDel_dt(LocalDateTime del_dt) {
        this.del_dt = del_dt;
    }

    public void addMemberRole(Member_Role member_role) {
        member_role_set.add(member_role);
    }

}
