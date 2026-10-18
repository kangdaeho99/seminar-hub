package com.seminarhub.batch.delivery.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import com.seminarhub.batch.delivery.processor.DeliveryItemProcessor;
import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:delivery-writer;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.show-sql=false",
        "spring.batch.jdbc.initialize-schema=always",
        "spring.batch.job.enabled=false"
})
class DeliveryJpaWriterIntegrationTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 9, 20, 0, 0);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("deliveryStatusUpdateAsyncJob")
    private Job asyncJob;

    @Autowired
    @Qualifier("deliveryStatusUpdateSyncJob")
    private Job syncJob;

    @Autowired
    @Qualifier("deliveryWriter")
    private JpaItemWriter<Delivery> writer;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockitoBean(name = "deliveryProcessor", enforceOverride = true)
    private DeliveryItemProcessor processor;

    @BeforeEach
    void setUp() throws Exception {
        jdbcTemplate.update("delete from delivery");
        jdbcTemplate.update("delete from member_seminar_item");
        when(processor.process(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery delivery = invocation.getArgument(0);
            delivery.updateStatus(DeliveryStatus.SHIPPING);
            return delivery;
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void commitsUpdatesAcrossChunkBoundariesWithTheJpaWriter(boolean async) throws Exception {
        seedDeliveries(501);

        JobExecution execution = execute(async ? asyncJob : syncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(step.getReadCount()).isEqualTo(501);
        assertThat(step.getWriteCount()).isEqualTo(501);
        assertThat(step.getSkipCount()).isZero();
        assertThat(jdbcTemplate.queryForList(
                "select delivery_status from delivery order by id", String.class))
                .hasSize(501).containsOnly("SHIPPING");
        assertThat(jdbcTemplate.queryForObject(
                "select tracking_number from delivery where id = 501", String.class))
                .isEqualTo("tracking-501");
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void mergesCachedItemsAfterRollbackAndLeavesTheSkippedDeliveryUnchanged(boolean async) throws Exception {
        seedDeliveries(3);
        when(processor.process(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery delivery = invocation.getArgument(0);
            if (delivery.getId() == 2L) {
                throw new DeliveryStatusUpdateException("simulated lookup failure");
            }
            delivery.updateStatus(DeliveryStatus.SHIPPING);
            return delivery;
        });

        JobExecution execution = execute(async ? asyncJob : syncJob);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        StepExecution step = execution.getStepExecutions().iterator().next();
        assertThat(step.getWriteCount()).isEqualTo(2);
        assertThat(step.getRollbackCount()).isPositive();
        assertThat(step.getProcessSkipCount()).isEqualTo(async ? 0 : 1);
        assertThat(step.getWriteSkipCount()).isEqualTo(async ? 1 : 0);
        assertThat(jdbcTemplate.queryForList(
                "select delivery_status from delivery order by id", String.class))
                .containsExactly("SHIPPING", "PENDING", "SHIPPING");
        verify(processor, times(1)).process(argThat(item -> item.getId() == 1L));
        verify(processor, times(3)).process(argThat(item -> item.getId() == 2L));
        verify(processor, times(1)).process(argThat(item -> item.getId() == 3L));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void flushesAndClearsButDoesNotCommitOutsideTheChunkTransaction(boolean detached) {
        seedDeliveries(1);

        new TransactionTemplate(transactionManager).executeWithoutResult(transaction -> {
            EntityManager entityManager =
                    EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
            assertThat(entityManager).isNotNull();
            Delivery delivery = entityManager.find(Delivery.class, 1L);
            if (detached) {
                entityManager.detach(delivery);
            }
            assertThat(entityManager.contains(delivery)).isEqualTo(!detached);
            delivery.updateStatus(DeliveryStatus.SHIPPING);

            writer.write(new Chunk<>(List.of(delivery)));

            assertThat(entityManager.contains(delivery)).isFalse();
            // Same-transaction JDBC sees the flushed SQL, even before commit.
            assertThat(jdbcTemplate.queryForObject(
                    "select delivery_status from delivery where id = 1", String.class))
                    .isEqualTo("SHIPPING");
            transaction.setRollbackOnly();
        });

        assertThat(jdbcTemplate.queryForObject(
                "select delivery_status from delivery where id = 1", String.class))
                .isEqualTo("PENDING");
    }

    private JobExecution execute(Job job) throws Exception {
        return jobLauncher.run(job, new JobParametersBuilder()
                .addLocalDateTime("startAt", START)
                .addLocalDateTime("endAt", START.plusDays(1))
                .addString("testRun", UUID.randomUUID().toString())
                .toJobParameters());
    }

    private void seedDeliveries(int count) {
        // Parent associations are not traversed by this job; their foreign keys are disabled in the model.
        List<Object[]> items = IntStream.rangeClosed(1, count)
                .mapToObj(id -> new Object[] {id, 1L, 1L, "ORDERED", START})
                .toList();
        jdbcTemplate.batchUpdate("""
                insert into member_seminar_item
                    (id, member_seminar_id, seminar_id, member_seminar_item_status, created_at)
                values (?, ?, ?, ?, ?)
                """, items);
        List<Object[]> deliveries = IntStream.rangeClosed(1, count)
                .mapToObj(id -> new Object[] {id, id, "PENDING", "tracking-" + id, START})
                .toList();
        jdbcTemplate.batchUpdate("""
                insert into delivery (id, member_seminar_item_id, delivery_status, tracking_number, created_at)
                values (?, ?, ?, ?, ?)
                """, deliveries);
    }
}
