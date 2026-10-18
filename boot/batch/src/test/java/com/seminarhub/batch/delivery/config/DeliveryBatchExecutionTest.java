package com.seminarhub.batch.delivery.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.seminarhub.batch.delivery.controller.BatchJobController;
import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import com.seminarhub.batch.delivery.processor.DeliveryItemProcessor;
import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.service.DeliveryService;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.configuration.support.JdbcDefaultBatchConfiguration;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringJUnitConfig({DeliveryBatchConfig.class, DeliverySyncBatchConfig.class,
        BatchJobController.class, DeliveryBatchExecutionTest.Infrastructure.class})
class DeliveryBatchExecutionTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 9, 20, 0, 0);
    private static final LocalDateTime END = START.plusDays(1);

    @Autowired
    @Qualifier("deliveryStatusUpdateAsyncJob")
    private Job asyncJob;

    @Autowired
    @Qualifier("deliveryStatusUpdateSyncJob")
    private Job syncJob;

    @TestBean(methodName = "testJobRepository", enforceOverride = true)
    private JobRepository repository;

    @MockitoBean
    private DeliveryService service;

    @MockitoBean(name = "deliveryProcessor", enforceOverride = true)
    private DeliveryItemProcessor processor;

    @MockitoBean
    private EntityManagerFactory entityManagerFactory;

    @MockitoBean(name = "deliveryWriter", enforceOverride = true)
    private JpaItemWriter<Delivery> writer;

    private final List<Thread> processingThreads = new CopyOnWriteArrayList<>();
    private final List<Boolean> processingTransactions = new CopyOnWriteArrayList<>();
    private final List<List<Delivery>> writes = new ArrayList<>();
    private final List<Thread> writingThreads = new ArrayList<>();
    private final List<Boolean> writingTransactions = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        when(processor.process(any(Delivery.class))).thenAnswer(invocation -> {
            processingThreads.add(Thread.currentThread());
            processingTransactions.add(TransactionSynchronizationManager.isActualTransactionActive());
            Delivery item = invocation.getArgument(0);
            item.updateStatus(DeliveryStatus.SHIPPING);
            return item;
        });
        doAnswer(invocation -> {
            Chunk<Delivery> items = invocation.getArgument(0);
            writes.add(List.copyOf(items.getItems()));
            writingThreads.add(Thread.currentThread());
            writingTransactions.add(TransactionSynchronizationManager.isActualTransactionActive());
            return null;
        }).when(writer).write(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void processesAndWritesAcrossChunkBoundariesOnTheExpectedThreads(boolean async) throws Exception {
        stubPages(501);
        Thread stepThread = Thread.currentThread();

        JobExecution execution = execute(async ? asyncJob : syncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(step.getStepName()).isEqualTo(async
                ? "deliveryStatusUpdateAsyncStep" : "deliveryStatusUpdateSyncStep");
        assertThat(step.getReadCount()).isEqualTo(501);
        assertThat(step.getWriteCount()).isEqualTo(501);
        assertThat(writes).extracting(List::size).containsExactly(500, 1);
        assertThat(writes.stream().flatMap(List::stream).map(Delivery::getId).toList())
                .containsExactlyElementsOf(LongStream.rangeClosed(1, 501).boxed().toList());
        assertThat(writes.stream().flatMap(List::stream).map(Delivery::getDeliveryStatus).toList())
                .containsOnly(DeliveryStatus.SHIPPING);
        assertThat(processingThreads).hasSize(501).allSatisfy(thread -> {
            if (async) {
                assertThat(thread).isNotSameAs(stepThread);
                assertThat(thread.getName()).startsWith("async-batch-");
            } else {
                assertThat(thread).isSameAs(stepThread);
            }
        });
        assertThat(processingTransactions).containsOnly(!async);
        assertThat(writingThreads).hasSize(2).allSatisfy(thread -> assertThat(thread).isSameAs(stepThread));
        assertThat(writingTransactions).containsOnly(true);
    }

    @Test
    void startsWithAFreshReaderForEachStepExecution() throws Exception {
        stubPages(3);

        for (Job job : List.of(syncJob, asyncJob, syncJob)) {
            assertThat(execute(job).getStatus()).isEqualTo(BatchStatus.COMPLETED);
        }

        assertThat(writes).hasSize(3).allSatisfy(items ->
                assertThat(items).extracting(Delivery::getId).containsExactly(1L, 2L, 3L));
        verify(service, times(3)).findNextBatch(START, END, 0L, 500);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void skipsExternalApiFailuresAtTheExpectedStage(boolean async) throws Exception {
        stubPages(3);
        when(processor.process(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery item = invocation.getArgument(0);
            if (item.getId() == 2L) {
                throw new DeliveryStatusUpdateException("simulated external API failure");
            }
            item.updateStatus(DeliveryStatus.SHIPPING);
            return item;
        });

        JobExecution execution = execute(async ? asyncJob : syncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(step.getWriteCount()).isEqualTo(2);
        assertThat(step.getProcessSkipCount()).isEqualTo(async ? 0 : 1);
        assertThat(step.getWriteSkipCount()).isEqualTo(async ? 1 : 0);
        verify(processor, times(3)).process(argThat(item -> item.getId() == 2L));
        // Writer recovery must reuse cached results, not call the external API again.
        verify(processor, times(1)).process(argThat(item -> item.getId() == 1L));
        verify(processor, times(1)).process(argThat(item -> item.getId() == 3L));
        assertThat(step.getRollbackCount()).isPositive();
        assertThat(writes.stream().flatMap(List::stream).map(Delivery::getId).toList())
                .containsExactly(1L, 3L);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void retriesTransientApiFailuresAndWritesTheRecoveredItem(boolean async) throws Exception {
        stubPages(3);
        AtomicInteger attempts = new AtomicInteger();
        when(processor.process(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery item = invocation.getArgument(0);
            if (item.getId() == 2L && attempts.incrementAndGet() < 3) {
                throw new DeliveryStatusUpdateException("transient external API failure");
            }
            item.updateStatus(DeliveryStatus.SHIPPING);
            return item;
        });

        JobExecution execution = execute(async ? asyncJob : syncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(attempts.get()).isEqualTo(3);
        assertThat(step.getProcessSkipCount()).isZero();
        assertThat(step.getWriteSkipCount()).isZero();
        assertThat(step.getWriteCount()).isEqualTo(3);
        if (async) {
            // Recovered API failures stay inside the Future, outside the chunk transaction.
            assertThat(step.getRollbackCount()).isZero();
        } else {
            assertThat(step.getRollbackCount()).isGreaterThanOrEqualTo(2);
        }
        assertThat(writes.stream().flatMap(List::stream).map(Delivery::getId).toList())
                .containsExactly(1L, 2L, 3L);
        // processorNonTransactional caches successful results across chunk retries.
        verify(processor, times(1)).process(argThat(item -> item.getId() == 1L));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void failsWithoutRetryingOrSkippingUnrelatedExceptions(boolean async) throws Exception {
        stubPages(1);
        when(processor.process(any(Delivery.class)))
                .thenThrow(new IllegalStateException("unexpected processing failure"));

        JobExecution execution = execute(async ? asyncJob : syncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.FAILED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(step.getProcessSkipCount()).isZero();
        assertThat(step.getWriteSkipCount()).isZero();
        assertThat(step.getWriteCount()).isZero();
        verify(processor, times(1)).process(any(Delivery.class));
        assertThat(writes).isEmpty();
    }

    @Test
    void failsWithoutRetryingOrSkippingInterruptedAsyncLookups() throws Exception {
        stubPages(1);
        when(processor.process(any(Delivery.class))).thenThrow(
                new DeliveryStatusUpdateException("lookup interrupted", new InterruptedException()));

        JobExecution execution = execute(asyncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.FAILED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(step.getProcessSkipCount()).isZero();
        assertThat(step.getWriteSkipCount()).isZero();
        assertThat(step.getWriteCount()).isZero();
        verify(processor, times(1)).process(any(Delivery.class));
        assertThat(writes).isEmpty();
    }

    private void stubPages(int total) {
        when(service.findNextBatch(eq(START), eq(END), anyLong(), anyInt())).thenAnswer(invocation -> {
            long lastId = invocation.getArgument(2);
            int pageSize = invocation.getArgument(3);
            return LongStream.rangeClosed(lastId + 1, Math.min(lastId + pageSize, total))
                    .mapToObj(id -> Delivery.builder().id(id).deliveryStatus(DeliveryStatus.PENDING).build())
                    .toList();
        });
    }

    private JobExecution execute(Job job) throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addLocalDateTime("startAt", START)
                .addLocalDateTime("endAt", END)
                .toJobParameters();
        // Execute the real Job/Step with a fresh test execution; no database or restart metadata is used.
        JobInstance instance = repository.createJobInstance(job.getName(), parameters);
        JobExecution execution = repository.createJobExecution(instance, parameters, new ExecutionContext());
        job.execute(execution);
        return execution;
    }

    static JobRepository testJobRepository() {
        return new ResourcelessJobRepository();
    }

    @Configuration
    static class Infrastructure extends JdbcDefaultBatchConfiguration {
        @Bean
        PlatformTransactionManager transactionManager() {
            return new ResourcelessTransactionManager();
        }
    }
}
