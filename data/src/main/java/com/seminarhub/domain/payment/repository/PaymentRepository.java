package com.seminarhub.domain.payment.repository;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.repository.querydsl.PaymentRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
    Optional<Payment> findByIdAndDeletedAtIsNull(Long id);
    List<Payment> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
