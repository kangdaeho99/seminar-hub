package com.seminarhub.domain.seminar.service;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.time.LocalDateTime;
import java.util.List;
public record MemberSeminarPaymentHistorySearchQuery(Long id, List<Long> ids, Long amount, Long amountMin, Long amountMax,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static MemberSeminarPaymentHistorySearchQuery empty() { return new MemberSeminarPaymentHistorySearchQuery(
            null, null, null, null, null, null, null, null, null, null, null, null, null, null); }
}
