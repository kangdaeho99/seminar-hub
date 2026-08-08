package com.seminarhub.controller;

import com.seminarhub.dto.BatchJobResponse;
import com.seminarhub.dto.DeliveryStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Spring Batch Job을 REST API로 수동 트리거하는 컨트롤러.
 *
 * POST /batch/delivery/status-update
 *   → deliveryStatusUpdateJob 실행
 */
@Slf4j
@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchJobController {

    private final JobLauncher jobLauncher;
    private final Job deliveryStatusUpdateJob;

    /**
     * 배송 상태 업데이트 Job 수동 실행
     *
     * @param request 배송 상태 업데이트 요청 DTO (startAt, endAt)
     * @return Job 실행 결과 (jobId, status, startTime)
     */
    @PostMapping("/delivery/status-update")
    public ResponseEntity<BatchJobResponse> runDeliveryStatusUpdateJob(
            @RequestBody(required = false) DeliveryStatusUpdateRequest request) {
        try {
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addLocalDateTime("startAt", request.getStartAt());
            builder.addLocalDateTime("endAt", request.getEndAt());

            JobParameters jobParameters = builder.toJobParameters();

            log.info("배송 상태 업데이트 Job 수동 실행 요청: params={}", jobParameters);

            JobExecution jobExecution = jobLauncher.run(deliveryStatusUpdateJob, jobParameters);

            BatchJobResponse response = BatchJobResponse.success(
                    jobExecution.getJobId(),
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus().name(),
                    jobExecution.getStartTime()
            );

            log.info("Job 실행 완료: jobId={}, status={}", jobExecution.getJobId(), jobExecution.getStatus());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Job 실행 중 오류 발생: {}", e.getMessage(), e);
            BatchJobResponse response = BatchJobResponse.failure(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
