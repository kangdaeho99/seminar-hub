package com.seminarhub.core.entity;


import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "member")
    private List<Member_Seminar> member_seminar_list;

    public void setMember_nickname(String member_nickname){
        this.member_nickname = member_nickname;
    }
    public void addMemberRole(Member_Role member_role){
        member_role_set.add(member_role);
    }


}
