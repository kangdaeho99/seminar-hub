package com.seminarhub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import com.seminarhub.dto.BatchJobResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @return Job 실행 결과 (jobId, status, startTime)
     */
    @PostMapping("/delivery/status-update")
    public ResponseEntity<BatchJobResponse> runDeliveryStatusUpdateJob() {
        try {
            // JobParameters에 실행 시각을 포함해 동일 파라미터로 중복 실행되지 않도록 구분
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestedAt", LocalDateTime.now().toString())
                    .toJobParameters();

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
