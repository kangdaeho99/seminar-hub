package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member_list"})
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_no;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<Member_Role> member_list;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType role_type;

}
