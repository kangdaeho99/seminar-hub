package com.seminarhub.global.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditSearchQuery {
    Long id();

    List<Long> ids();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    LocalDateTime deletedAt();

    LocalDateTime createdStartAt();

    LocalDateTime createdEndAt();

    LocalDateTime updatedStartAt();

    LocalDateTime updatedEndAt();

    LocalDateTime deletedStartAt();

    LocalDateTime deletedEndAt();

    default boolean hasDeletedAtCondition() {
        return deletedAt() != null || deletedStartAt() != null || deletedEndAt() != null;
    }
}
