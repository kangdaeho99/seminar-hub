package com.seminarhub.domain.seminar.service;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDateTime;
import java.util.List;
public record MemberSeminarSearchQuery(Long id, List<Long> ids, Long memberId, List<Long> memberIds,
        Long seminarId, List<Long> seminarIds,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static MemberSeminarSearchQuery empty() { return new MemberSeminarSearchQuery(null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null); }
}
