package com.seminarhub.batch.delivery.dto;

import java.time.LocalDateTime;

/**
 * Batch Job 실행 결과를 담는 Response DTO.
 */
public record BatchJobResponse(
        Long jobId,
        String jobName,
        String status,
        LocalDateTime startTime,
        String error
) {
    public static BatchJobResponse success(Long jobId, String jobName, String status, LocalDateTime startTime) {
        return new BatchJobResponse(jobId, jobName, status, startTime, null);
    }

    public static BatchJobResponse failure(String error) {
        return new BatchJobResponse(null, null, "FAILED", null, error);
    }
}
