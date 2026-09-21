package com.seminarhub.domain.seminar.repository;
import com.seminarhub.domain.seminar.domain.MemberSeminarPaymentHistory;
import com.seminarhub.domain.seminar.repository.querydsl.MemberSeminarPaymentHistoryRepositoryCustom;
import java.util.Collection; import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberSeminarPaymentHistoryRepository extends JpaRepository<MemberSeminarPaymentHistory, Long>, MemberSeminarPaymentHistoryRepositoryCustom {
    Optional<MemberSeminarPaymentHistory> findByIdAndDeletedAtIsNull(Long id); List<MemberSeminarPaymentHistory> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
