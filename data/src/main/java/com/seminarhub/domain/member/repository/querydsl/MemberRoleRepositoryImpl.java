package com.seminarhub.domain.member.repository.querydsl;

import static com.seminarhub.domain.member.domain.QMemberRole.memberRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.member.domain.MemberRole;
import com.seminarhub.domain.member.service.MemberRoleSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class MemberRoleRepositoryImpl extends QuerydslRepositorySupport<MemberRole> implements MemberRoleRepositoryCustom {
    public MemberRoleRepositoryImpl(JPAQueryFactory f) { super(f, memberRole, memberRole.id, memberRole.createdAt, memberRole.updatedAt, memberRole.deletedAt); }
    public List<MemberRole> search(MemberRoleSearchQuery q) { return findAll(condition(q)); }
    public Page<MemberRole> search(MemberRoleSearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<MemberRole> search(MemberRoleSearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }
    private BooleanBuilder condition(MemberRoleSearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (q.memberId() != null) b.and(memberRole.member.id.eq(q.memberId()));
        if (!CollectionUtils.isEmpty(q.memberIds())) b.and(memberRole.member.id.in(q.memberIds()));
        if (q.roleId() != null) b.and(memberRole.role.id.eq(q.roleId()));
        if (!CollectionUtils.isEmpty(q.roleIds())) b.and(memberRole.role.id.in(q.roleIds()));
        return b;
    }
    private ComparableExpressionBase<?> sort(String n) { return switch (n) {
        case "id" -> memberRole.id; case "memberId" -> memberRole.member.id; case "roleId" -> memberRole.role.id;
        case "createdAt" -> memberRole.createdAt; case "updatedAt" -> memberRole.updatedAt;
        case "deletedAt" -> memberRole.deletedAt; default -> null; }; }
}
