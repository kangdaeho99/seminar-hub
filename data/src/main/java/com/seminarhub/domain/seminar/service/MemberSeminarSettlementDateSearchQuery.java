package com.seminarhub.domain.seminar.service;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public record MemberSeminarSettlementDateSearchQuery(Long id, List<Long> ids, Long memberSeminarId,
        List<Long> memberSeminarIds, LocalDate date, LocalDate dateFrom, LocalDate dateTo,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static MemberSeminarSettlementDateSearchQuery empty() { return new MemberSeminarSettlementDateSearchQuery(
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null); }
}
