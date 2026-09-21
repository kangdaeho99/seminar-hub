package com.seminarhub.domain.member.repository.querydsl;

import static com.seminarhub.domain.member.domain.QRole.role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.member.domain.Role;
import com.seminarhub.domain.member.service.RoleSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class RoleRepositoryImpl extends QuerydslRepositorySupport<Role> implements RoleRepositoryCustom {
    public RoleRepositoryImpl(JPAQueryFactory f) { super(f, role, role.id, role.createdAt, role.updatedAt, role.deletedAt); }
    public List<Role> search(RoleSearchQuery q) { return findAll(condition(q)); }
    public Page<Role> search(RoleSearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<Role> search(RoleSearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }
    private BooleanBuilder condition(RoleSearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (q.roleType() != null) b.and(role.roleType.eq(q.roleType()));
        if (!CollectionUtils.isEmpty(q.roleTypes())) b.and(role.roleType.in(q.roleTypes()));
        return b;
    }
    private ComparableExpressionBase<?> sort(String n) { return switch (n) {
        case "id" -> role.id; case "roleType" -> role.roleType; case "createdAt" -> role.createdAt;
        case "updatedAt" -> role.updatedAt; case "deletedAt" -> role.deletedAt; default -> null; }; }
}
