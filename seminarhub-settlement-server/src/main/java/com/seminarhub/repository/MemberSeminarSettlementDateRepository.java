package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminarSettlementDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberSeminarSettlementDateRepository extends JpaRepository<MemberSeminarSettlementDate, Long> {

    @Query("""
            select settlementDate
            from MemberSeminarSettlementDate settlementDate
            where settlementDate.id = :settlementDateId
            """)
    Optional<MemberSeminarSettlementDate> findByIdWithoutLock(@Param("settlementDateId") Long settlementDateId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select settlementDate
            from MemberSeminarSettlementDate settlementDate
            where settlementDate.id = :settlementDateId
            """)
    Optional<MemberSeminarSettlementDate> findByIdForUpdate(@Param("settlementDateId") Long settlementDateId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
            select settlementDate
            from MemberSeminarSettlementDate settlementDate
            join fetch settlementDate.memberSeminar memberSeminar
            join fetch memberSeminar.seminar seminar
            where settlementDate.date between :startAt and :endAt
            and memberSeminar.del_dt is null
            and seminar.del_dt is null
            """)
    List<MemberSeminarSettlementDate> findAllByDateBetweenForShareLock(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt
    );
}
