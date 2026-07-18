package com.seminarhub.config;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.enums.DeliveryStatus;
import com.seminarhub.repository.DeliveryRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeliveryBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DeliveryRepository deliveryRepository;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 100;

    // -------------------------------------------------------------------------
    // Job
    // -------------------------------------------------------------------------

    @Bean
    public Job deliveryStatusUpdateJob() {
        return new JobBuilder("deliveryStatusUpdateJob", jobRepository)
                .start(deliveryStatusUpdateStep())
                .build();
    }

    // -------------------------------------------------------------------------
    // Step
    // -------------------------------------------------------------------------

    @Bean
    public Step deliveryStatusUpdateStep() {
        return new StepBuilder("deliveryStatusUpdateStep", jobRepository)
                .<Delivery, Delivery>chunk(CHUNK_SIZE, transactionManager)
                .reader(deliveryReader())
                .processor(deliveryProcessor())
                .writer(deliveryWriter())
                .build();
    }

    // -------------------------------------------------------------------------
    // Reader — PENDING 상태 배송 건을 id ASC 정렬로 페이징 조회
    // -------------------------------------------------------------------------

    @Bean
    @StepScope
    public RepositoryItemReader<Delivery> deliveryReader() {
        return new RepositoryItemReaderBuilder<Delivery>()
                .name("deliveryReader")
                .repository(deliveryRepository)
                .methodName("findByDeliveryStatus")
                .arguments(DeliveryStatus.PENDING)
                .pageSize(CHUNK_SIZE)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    // -------------------------------------------------------------------------
    // Processor — 외부 API 결과에 따라 상태 변경
    // TODO: Math.random() 시뮬레이션을 실제 외부 API 호출 로직으로 대체
    // -------------------------------------------------------------------------

    @Bean
    public ItemProcessor<Delivery, Delivery> deliveryProcessor() {
        return delivery -> {
            log.info("배송상태 검증: ID={}, 운송장번호={}, 택배사={}",
                    delivery.getId(), delivery.getTrackingNumber(), delivery.getCourierCompany());

            // TODO: 실제 외부 API 호출 로직 구현 (WebClient, FeignClient 등 활용)
            // 현재는 API 응답 결과를 임의로 시뮬레이션합니다.
            boolean isApiStatusDelivered = Math.random() > 0.5;

            if (isApiStatusDelivered) {
                // API 결과가 '배송완료'라면 엔티티 상태 업데이트 후 반환 → Writer로 전달
                delivery.updateStatus(DeliveryStatus.DELIVERED);
                return delivery;
            }

            // 여전히 '대기중'이라면 null 반환 → Writer 단계에서 필터링됨
            return null;
        };
    }

    // -------------------------------------------------------------------------
    // Writer — 청크 단위로 JPA Dirty Checking / Merge를 통해 UPDATE 수행
    // -------------------------------------------------------------------------

    @Bean
    public JpaItemWriter<Delivery> deliveryWriter() {
        return new JpaItemWriterBuilder<Delivery>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
