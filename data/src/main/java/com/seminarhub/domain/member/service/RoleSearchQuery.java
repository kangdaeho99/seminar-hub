package com.seminarhub.domain.member.service;

import com.seminarhub.domain.member.enums.RoleType;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDateTime;
import java.util.List;

public record RoleSearchQuery(
        Long id, List<Long> ids, RoleType roleType, List<RoleType> roleTypes,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static RoleSearchQuery empty() { return new RoleSearchQuery(null, null, null, null, null, null, null,
            null, null, null, null, null, null); }
}
