package com.seminarhub.domain.member.service;

import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDateTime;
import java.util.List;

public record MemberRoleSearchQuery(
        Long id, List<Long> ids, Long memberId, List<Long> memberIds, Long roleId, List<Long> roleIds,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static MemberRoleSearchQuery empty() { return new MemberRoleSearchQuery(null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null); }
}
