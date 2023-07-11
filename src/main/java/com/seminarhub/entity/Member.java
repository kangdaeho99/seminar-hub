package com.seminarhub.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member_role_set"})
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

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Member_Role> member_role_set  = new HashSet<>();

    public void setMember_nickname(String member_nickname){
        this.member_nickname = member_nickname;
    }
    public void addMemberRole(Member_Role member_role){
        member_role_set.add(member_role);
    }

    @Override
    public String toString() {
        return "Member(member_no=" + member_no +
                ", member_id=" + member_id +
                ", member_password=" + member_password +
                ", member_nickname=" + member_nickname +
                ", member_from_social=" + member_from_social +
                ", del_dt=" + del_dt +
                ")";
    }


}
