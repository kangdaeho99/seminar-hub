package com.seminarhub.config;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.enums.DeliveryStatus;
import com.seminarhub.util.DeliveryStatusExternalApiUtil;
import com.seminarhub.repository.DeliveryRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.seminarhub.exception.DeliveryStatusUpdateException;
import com.seminarhub.listener.DeliveryChunkListener;
import com.seminarhub.listener.DeliveryProcessListener;
import com.seminarhub.listener.DeliveryReadListener;
import com.seminarhub.listener.DeliverySkipListener;
import com.seminarhub.listener.DeliveryWriteListener;
import com.seminarhub.policy.DeliverySkipPolicy;
import com.seminarhub.reader.DeliveryKeysetItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import com.seminarhub.validator.DeliveryJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeliveryBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DeliveryRepository deliveryRepository;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 500;

    @Bean
    public JobParametersValidator deliveryJobParametersValidator() {
        return new DeliveryJobParametersValidator();
    }

    @Bean
    public Job deliveryStatusUpdateJob() {
        return new JobBuilder("deliveryStatusUpdateJob", jobRepository)
                .validator(deliveryJobParametersValidator())
                .start(deliveryStatusUpdateStep())
                .build();
    }

    @Bean
    public Step deliveryStatusUpdateStep() {
        return new StepBuilder("deliveryStatusUpdateStep", jobRepository)
                .<Delivery, Delivery>chunk(CHUNK_SIZE, transactionManager)
                .reader(deliveryReader(null, null))
                // .reader(deliveryCursorReader(null, null))
                .processor(deliveryProcessor())
                .writer(deliveryWriter())
                .faultTolerant()
                .retry(DeliveryStatusUpdateException.class)
                .retryLimit(2)
                .skipPolicy(new DeliverySkipPolicy())
                .listener(new DeliveryChunkListener())
                .listener(new DeliveryReadListener())
                .listener(new DeliveryProcessListener())
                .listener(new DeliveryWriteListener())
                .listener(new DeliverySkipListener())
                .build();
    }

    @Bean
    @StepScope
    public DeliveryKeysetItemReader deliveryReader(
            @Value("#{jobParameters['startAt']}") LocalDateTime startAt,
            @Value("#{jobParameters['endAt']}") LocalDateTime endAt) {

        return new DeliveryKeysetItemReader(
                deliveryRepository,
                startAt,
                endAt,
                CHUNK_SIZE);
    }

    @Bean
    public ItemProcessor<Delivery, Delivery> deliveryProcessor() {
        return delivery -> {
            DeliveryStatus externalStatus = DeliveryStatusExternalApiUtil.checkDeliveryStatus();
            delivery.updateStatus(externalStatus);
            return delivery;
        };
    }

    // @Bean
    // public ItemProcessor<Delivery, Delivery> deliveryProcessor() {
    //     return delivery -> {
    //         try {
    //             if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING) {
    //                 return null;
    //             }

    //             DeliveryStatus externalStatus = DeliveryStatusExternalApiUtil.checkDeliveryStatus();

    //             if (externalStatus == DeliveryStatus.SHIPPING || externalStatus == DeliveryStatus.DELIVERED) {
    //                 delivery.updateStatus(externalStatus);
    //                 return delivery;
    //             }

    //             return null;
    //         } catch (Exception e) {
    //             log.error("Error processing delivery: {}", e.getMessage(), e);
    //             return null;
    //         }
    //     };
    // }

    @Bean
    public JpaItemWriter<Delivery> deliveryWriter() {
        return new JpaItemWriterBuilder<Delivery>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


    //     @Bean
    // @StepScope
    // public RepositoryItemReader<Delivery> deliveryReader(
    //         @Value("#{jobParameters['startAt']}") LocalDateTime startAt,
    //         @Value("#{jobParameters['endAt']}") LocalDateTime endAt) {

    //     return new RepositoryItemReaderBuilder<Delivery>()
    //             .name("deliveryReader")
    //             .repository(deliveryRepository)
    //             .methodName("findByDeliveryStatusAndInstDtBetween")
    //             .arguments(Arrays.asList(DeliveryStatus.PENDING, startAt, endAt))
    //             .pageSize(CHUNK_SIZE)
    //             .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
    //             .build();
    // }
}
