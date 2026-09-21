package com.seminarhub.domain.payment.service;
import com.seminarhub.global.repository.AuditSearchQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public record PaymentSearchQuery(Long id, List<Long> ids, String email, BigDecimal paymentAmount,
        BigDecimal paymentAmountMin, BigDecimal paymentAmountMax,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static PaymentSearchQuery empty() { return new PaymentSearchQuery(null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null); }
}
