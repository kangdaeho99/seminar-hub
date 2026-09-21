package com.seminarhub.domain.seminar.service;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDateTime;
import java.util.List;
public record SeminarSearchQuery(Long id, List<Long> ids, String name, String explanation, Long price,
        Long priceMin, Long priceMax, Long maxParticipants, Long participantsCount,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static SeminarSearchQuery empty() { return new SeminarSearchQuery(null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null); }
}
