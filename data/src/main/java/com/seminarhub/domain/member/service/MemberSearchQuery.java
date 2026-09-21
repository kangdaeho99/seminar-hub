package com.seminarhub.domain.member.service;

import com.seminarhub.global.repository.AuditSearchQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MemberSearchQuery(
        Long id, List<Long> ids, String email, String password, String nickname, Boolean fromSocial,
        BigDecimal chargedMoney, BigDecimal chargedMoneyMin, BigDecimal chargedMoneyMax,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
        LocalDateTime createdStartAt, LocalDateTime createdEndAt, LocalDateTime updatedStartAt,
        LocalDateTime updatedEndAt, LocalDateTime deletedStartAt, LocalDateTime deletedEndAt) implements AuditSearchQuery {
    public static MemberSearchQuery empty() { return new MemberSearchQuery(null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null); }
}
