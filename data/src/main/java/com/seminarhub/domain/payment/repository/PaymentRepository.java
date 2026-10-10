package com.seminarhub.domain.payment.repository;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.enums.PaymentStatus;
import com.seminarhub.domain.payment.repository.querydsl.PaymentRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
    @Query("""
            select p from Payment p
            where p.memberSeminarItem.id = :memberSeminarItemId
              and p.paymentStatus = :paymentStatus
              and p.deletedAt is null
            """)
    Optional<Payment> findByMemberSeminarItemIdAndStatus(
            @Param("memberSeminarItemId") Long memberSeminarItemId,
            @Param("paymentStatus") PaymentStatus paymentStatus);

    Optional<Payment> findByIdAndDeletedAtIsNull(Long id);
    List<Payment> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
