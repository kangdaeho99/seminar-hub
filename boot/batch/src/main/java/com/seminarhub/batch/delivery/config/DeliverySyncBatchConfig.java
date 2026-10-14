package com.seminarhub.batch.delivery.config;

import com.seminarhub.batch.delivery.exception.DeliveryStatusUpdateException;
import com.seminarhub.batch.delivery.listener.DeliveryChunkListener;
import com.seminarhub.batch.delivery.listener.DeliveryReadListener;
import com.seminarhub.batch.delivery.listener.DeliverySyncProcessListener;
import com.seminarhub.batch.delivery.listener.DeliverySyncSkipListener;
import com.seminarhub.batch.delivery.listener.DeliverySyncWriteListener;
import com.seminarhub.batch.delivery.policy.DeliverySkipPolicy;
import com.seminarhub.batch.delivery.processor.DeliveryItemProcessor;
import com.seminarhub.batch.delivery.reader.DeliveryKeysetItemReader;
import com.seminarhub.batch.delivery.writer.DeliveryItemWriter;
import com.seminarhub.domain.delivery.domain.Delivery;
import java.util.Map;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DeliverySyncBatchConfig {

    @Bean
    public RetryPolicy deliverySyncRetryPolicy() {
        return new SimpleRetryPolicy(3, Map.of(DeliveryStatusUpdateException.class, true), true);
    }

    @Bean
    public Job deliveryStatusUpdateSyncJob(
            JobRepository jobRepository,
            @Qualifier("deliveryJobParametersValidator") JobParametersValidator validator,
            @Qualifier("deliveryStatusUpdateSyncStep") Step step) {
        return new JobBuilder("deliveryStatusUpdateSyncJob", jobRepository)
                .validator(validator)
                .start(step)
                .build();
    }

    @Bean
    public Step deliveryStatusUpdateSyncStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            DeliveryKeysetItemReader deliveryReader,
            DeliveryItemProcessor deliveryProcessor,
            DeliveryItemWriter deliveryWriter) {
        return new StepBuilder("deliveryStatusUpdateSyncStep", jobRepository)
                .<Delivery, Delivery>chunk(DeliveryBatchConfig.CHUNK_SIZE, transactionManager)
                .reader(deliveryReader)
                .processor(deliveryProcessor)
                .writer(deliveryWriter)
                .faultTolerant()
                .processorNonTransactional()
                .retryPolicy(deliverySyncRetryPolicy())
                .skipPolicy(new DeliverySkipPolicy())
                .listener(new DeliveryChunkListener())
                .listener(new DeliveryReadListener())
                .listener(new DeliverySyncProcessListener())
                .listener(new DeliverySyncWriteListener())
                .listener(new DeliverySyncSkipListener())
                .build();
    }
}
