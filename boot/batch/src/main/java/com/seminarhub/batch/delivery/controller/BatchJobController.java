package com.seminarhub.batch.delivery.controller;

import com.seminarhub.dto.ApiResponse;
import com.seminarhub.batch.delivery.dto.BatchJobResponse;
import com.seminarhub.batch.delivery.dto.DeliveryStatusUpdateRequest;
import com.seminarhub.error.InternalServerErrorException;
import com.seminarhub.error.origin.ErrorOrigin.SeminarHubError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Batch Job을 REST API로 수동 트리거하는 컨트롤러.
 *
 * POST /batch/delivery/status-update/async
 *   → deliveryStatusUpdateAsyncJob 실행
 * POST /batch/delivery/status-update/sync
 *   → deliveryStatusUpdateSyncJob 실행
 */
@Slf4j
@RestController
@RequestMapping("/batch")
public class BatchJobController {

    private final JobLauncher jobLauncher;
    private final Job deliveryStatusUpdateAsyncJob;
    private final Job deliveryStatusUpdateSyncJob;

    public BatchJobController(
            JobLauncher jobLauncher,
            @Qualifier("deliveryStatusUpdateAsyncJob") Job deliveryStatusUpdateAsyncJob,
            @Qualifier("deliveryStatusUpdateSyncJob") Job deliveryStatusUpdateSyncJob) {
        this.jobLauncher = jobLauncher;
        this.deliveryStatusUpdateAsyncJob = deliveryStatusUpdateAsyncJob;
        this.deliveryStatusUpdateSyncJob = deliveryStatusUpdateSyncJob;
    }

    /**
     * 배송 상태 업데이트 Job 수동 실행
     *
     * @param request 배송 상태 업데이트 요청 DTO (startAt, endAt)
     * @return Job 실행 결과 (jobId, status, startTime)
     */
    @PostMapping("/delivery/status-update/async")
    public ApiResponse<BatchJobResponse> runDeliveryStatusUpdateAsyncJob(
            @RequestBody(required = false) DeliveryStatusUpdateRequest request) {
        return runDeliveryStatusUpdateJob(deliveryStatusUpdateAsyncJob, request);
    }

    @PostMapping("/delivery/status-update/sync")
    public ApiResponse<BatchJobResponse> runDeliveryStatusUpdateSyncJob(
            @RequestBody(required = false) DeliveryStatusUpdateRequest request) {
        return runDeliveryStatusUpdateJob(deliveryStatusUpdateSyncJob, request);
    }

    private ApiResponse<BatchJobResponse> runDeliveryStatusUpdateJob(
            Job job, DeliveryStatusUpdateRequest request) {
        try {
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addLocalDateTime("startAt", request.getStartAt());
            builder.addLocalDateTime("endAt", request.getEndAt());

            JobParameters jobParameters = builder.toJobParameters();

            log.info("배송 상태 업데이트 Job 수동 실행 요청: jobName={}, params={}", job.getName(), jobParameters);

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            BatchJobResponse response = BatchJobResponse.success(
                    jobExecution.getId(),
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus().name(),
                    jobExecution.getStartTime()
            );

            log.info("Job 실행 완료: jobId={}, status={}", jobExecution.getId(), jobExecution.getStatus());
            return ApiResponse.ok(response);

        } catch (Exception e) {
            log.error("Job 실행 중 오류 발생: {}", e.getMessage(), e);
            throw new InternalServerErrorException(
                    SeminarHubError.DELIVERY_STATUS_UPDATE_FAILED,
                    e.getMessage(),
                    e);
        }
    }
}
