package com.seminarhub.batch.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seminarhub.global.error.BatchExceptionHandler;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BatchApiContractTest {

    private JobLauncher jobLauncher;
    private Job asyncJob;
    private Job syncJob;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        jobLauncher = mock(JobLauncher.class);
        asyncJob = mock(Job.class);
        syncJob = mock(Job.class);
        when(asyncJob.getName()).thenReturn("deliveryStatusUpdateAsyncJob");
        when(syncJob.getName()).thenReturn("deliveryStatusUpdateSyncJob");
        mockMvc = MockMvcBuilders.standaloneSetup(new BatchJobController(jobLauncher, asyncJob, syncJob))
                .setControllerAdvice(new BatchExceptionHandler())
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/async", "/sync"})
    void launchesTheSelectedJobWithOnlyTheRequestedDates(String suffix) throws Exception {
        Job job = "/sync".equals(suffix) ? syncJob : asyncJob;
        String jobName = job.getName();
        JobExecution execution = mock(JobExecution.class);
        JobInstance instance = mock(JobInstance.class);
        when(jobLauncher.run(eq(job), any(JobParameters.class))).thenReturn(execution);
        when(execution.getId()).thenReturn(1L);
        when(execution.getJobInstance()).thenReturn(instance);
        when(instance.getJobName()).thenReturn(jobName);
        when(execution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        when(execution.getStartTime()).thenReturn(LocalDateTime.of(2026, 9, 20, 12, 0));

        mockMvc.perform(post("/batch/delivery/status-update" + suffix)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"startAt":"2026-09-20T00:00:00","endAt":"2026-09-20T23:59:59"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.jobId").value(1))
                .andExpect(jsonPath("$.data.jobName").value(job.getName()))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        ArgumentCaptor<JobParameters> parameters = ArgumentCaptor.forClass(JobParameters.class);
        verify(jobLauncher).run(eq(job), parameters.capture());
        verifyNoMoreInteractions(jobLauncher);
        assertThat(parameters.getValue().parameters()).extracting(parameter -> parameter.name())
                .containsExactlyInAnyOrder("startAt", "endAt");
        assertThat(parameters.getValue().getLocalDateTime("startAt"))
                .isEqualTo(LocalDateTime.of(2026, 9, 20, 0, 0));
        assertThat(parameters.getValue().getLocalDateTime("endAt"))
                .isEqualTo(LocalDateTime.of(2026, 9, 20, 23, 59, 59));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/async", "/sync"})
    void returnsTheCommonErrorResponseWhenJobLaunchFails(String suffix) throws Exception {
        Job job = "/sync".equals(suffix) ? syncJob : asyncJob;
        when(jobLauncher.run(eq(job), any(JobParameters.class)))
                .thenThrow(new RuntimeException("launch failed"));

        mockMvc.perform(post("/batch/delivery/status-update" + suffix)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"startAt":"2026-09-20T00:00:00","endAt":"2026-09-20T23:59:59"}
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.origin").value("SEMINAR_HUB"))
                .andExpect(jsonPath("$.code").value(1002));
    }
}
