package com.seminarhub.config;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.MemberSeminar;
import com.seminarhub.entity.enums.DeliveryStatus;
import com.seminarhub.repository.DeliveryRepository;
import com.seminarhub.repository.MemberSeminarRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
class DeliveryBatchConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private MemberSeminarRepository memberSeminarRepository;

    @BeforeEach
    void setUp() {
        // 1. 이전 테스트 실행으로 남은 배치 메타데이터 초기화
        jobRepositoryTestUtils.removeJobExecutions();

        // 2. 기존 테스트 데이터 정리 (Delivery → MemberSeminar 순서로 삭제)
        deliveryRepository.deleteAllInBatch();
        memberSeminarRepository.deleteAllInBatch();

        // 3. MemberSeminar 더미 저장
        //    - member / seminar FK 는 ConstraintMode.NO_CONSTRAINT 이므로
        //      Member / Seminar 실제 저장 없이 MemberSeminar 만 저장 가능
        List<MemberSeminar> memberSeminars = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            memberSeminars.add(memberSeminarRepository.save(
                    MemberSeminar.builder().build()
            ));
        }

        // 4. PENDING 상태 Delivery 10건 저장
        for (int i = 0; i < 10; i++) {
            deliveryRepository.save(
                    Delivery.builder()
                            .memberSeminar(memberSeminars.get(i))
                            .deliveryStatus(DeliveryStatus.PENDING)
                            .trackingNumber("TRACKING-TEST-" + (i + 1))
                            .courierCompany("대한통운")
                            .build()
            );
        }
    }

    @AfterEach
    void tearDown() {
        deliveryRepository.deleteAllInBatch();
        memberSeminarRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("deliveryStatusUpdateJob — PENDING 배송 데이터를 읽어 정상적으로 Job이 완료된다")
    void deliveryStatusUpdateJob_success() throws Exception {
        // given
        org.springframework.batch.core.JobParameters jobParameters = new org.springframework.batch.core.JobParametersBuilder()
                .addString("startAt", "2026-01-01T15:00:00")
                .addString("endAt", "2026-01-01T16:00:00")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        /*
         * [추가 검증 로직 — 외부 API 연동 완료 후 활성화]
         *
         * 현재 Processor에 Math.random() 시뮬레이션이 있어 DELIVERED 전환 건수가 매번 달라집니다.
         * 실제 외부 API 연동 및 Processor 로직이 확정되면 아래 검증을 추가하세요.
         *
         * long deliveredCount = deliveryRepository.findAll().stream()
         *         .filter(d -> d.getDeliveryStatus() == DeliveryStatus.DELIVERED)
         *         .count();
         * assertThat(deliveredCount).isGreaterThan(0);
         *
         * long pendingCount = deliveryRepository.findAll().stream()
         *         .filter(d -> d.getDeliveryStatus() == DeliveryStatus.PENDING)
         *         .count();
         * assertThat(deliveredCount + pendingCount).isEqualTo(10);
         */
    }
}
