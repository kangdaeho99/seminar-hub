package com.seminarhub.domain.member.repository.querydsl;

import static com.seminarhub.domain.member.domain.QMember.member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.service.MemberSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class MemberRepositoryImpl extends QuerydslRepositorySupport<Member> implements MemberRepositoryCustom {
    public MemberRepositoryImpl(JPAQueryFactory f) { super(f, member, member.id, member.createdAt, member.updatedAt, member.deletedAt); }
    public List<Member> search(MemberSearchQuery q) { return findAll(condition(q)); }
    public Page<Member> search(MemberSearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<Member> search(MemberSearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }
    private BooleanBuilder condition(MemberSearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (StringUtils.hasText(q.email())) b.and(member.email.containsIgnoreCase(q.email()));
        if (StringUtils.hasText(q.password())) b.and(member.password.containsIgnoreCase(q.password()));
        if (StringUtils.hasText(q.nickname())) b.and(member.nickname.containsIgnoreCase(q.nickname()));
        if (q.fromSocial() != null) b.and(member.fromSocial.eq(q.fromSocial()));
        if (q.chargedMoney() != null) b.and(member.chargedMoney.eq(q.chargedMoney()));
        if (q.chargedMoneyMin() != null) b.and(member.chargedMoney.goe(q.chargedMoneyMin()));
        if (q.chargedMoneyMax() != null) b.and(member.chargedMoney.loe(q.chargedMoneyMax()));
        return b;
    }
    private ComparableExpressionBase<?> sort(String n) { return switch (n) {
        case "id" -> member.id; case "email" -> member.email; case "password" -> member.password;
        case "nickname" -> member.nickname; case "fromSocial" -> member.fromSocial;
        case "chargedMoney" -> member.chargedMoney; case "createdAt" -> member.createdAt;
        case "updatedAt" -> member.updatedAt; case "deletedAt" -> member.deletedAt; default -> null; }; }
}
