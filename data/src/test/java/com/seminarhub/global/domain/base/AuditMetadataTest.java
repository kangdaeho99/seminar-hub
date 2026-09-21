package com.seminarhub.global.domain.base;

import static org.assertj.core.api.Assertions.assertThat;

import com.seminarhub.data.DataTestApplication;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.repository.PaymentRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(
        classes = DataTestApplication.class,
        properties = {
            "spring.datasource.url=jdbc:h2:mem:audit;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=MEMBER",
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.jpa.hibernate.ddl-auto=create-drop"
        })
class AuditMetadataTest {

    @Autowired
    private PaymentRepository repository;

    @Test
    void createsUpdatesAndSoftDeletesAuditMetadata() {
        Payment payment = repository.saveAndFlush(Payment.builder()
                .email("audit@example.com")
                .paymentAmount(BigDecimal.TEN)
                .build());
        assertThat(payment.getCreatedAt()).isNotNull();

        payment.update("changed@example.com", null);
        repository.flush();
        assertThat(payment.getUpdatedAt()).isNotNull();

        payment.delete();
        repository.flush();
        assertThat(payment.getDeletedAt()).isNotNull();
        assertThat(repository.findByIdAndDeletedAtIsNull(payment.getId())).isEmpty();
    }
}
