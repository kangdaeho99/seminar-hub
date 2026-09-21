package com.seminarhub.domain.member.domain;

import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"memberRoles", "memberSeminars"})
public class Member extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String email;

    @Column(length = 500)
    private String password;

    @Column(length = 500)
    private String nickname;

    @Column
    @ColumnDefault("false")
    private boolean fromSocial;

    @Column(precision = 19, scale = 2)
    @ColumnDefault("0.0")
    private BigDecimal chargedMoney;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> memberRoles = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberSeminar> memberSeminars;

    public Member(long id) {
        this.id = id;
    }

    public void update(String email, String password, String nickname, Boolean fromSocial, BigDecimal chargedMoney) {
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        if (nickname != null) this.nickname = nickname;
        if (fromSocial != null) this.fromSocial = fromSocial;
        if (chargedMoney != null) this.chargedMoney = chargedMoney;
        markUpdated();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addMemberRole(MemberRole memberRole) {
        memberRoles.add(memberRole);
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}
