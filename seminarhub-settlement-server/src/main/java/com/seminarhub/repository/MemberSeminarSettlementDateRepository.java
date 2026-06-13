package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminarSettlementDate;
import com.seminarhub.dto.SettlementRecord;
import com.seminarhub.dto.SettlementRecordProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberSeminarSettlementDateRepository extends JpaRepository<MemberSeminarSettlementDate, Long> {

    @Query("""
            select memberSeminarSettlementDate
            from MemberSeminarSettlementDate memberSeminarSettlementDate
            where memberSeminarSettlementDate.memberSeminar.id = :memberSeminarId
            """)
    Optional<MemberSeminarSettlementDate> findByMemberSeminarIdWithoutLock(@Param("memberSeminarId") Long memberSeminarId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select memberSeminarSettlementDate
            from MemberSeminarSettlementDate memberSeminarSettlementDate
            where memberSeminarSettlementDate.id = :settlementDateId
            """)
    Optional<MemberSeminarSettlementDate> findByIdForUpdate(@Param("settlementDateId") Long settlementDateId);

    @Query(value = """
            SELECT ms.id AS memberSeminarId,
                   s.price AS price,
                   mssd.id AS settlementDateId
            FROM member_seminar_settlement_date mssd
            JOIN member_seminar ms ON mssd.member_seminar_id = ms.id
            JOIN seminar s ON ms.seminar_id = s.id
            WHERE mssd.date BETWEEN :startAt AND :endAt
              AND ms.del_dt IS NULL
              AND s.del_dt IS NULL
            FOR UPDATE OF mssd
            """, nativeQuery = true)
    List<SettlementRecordProjection> findAllByDateBetweenForExclusiveLockNative(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt);


    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE MemberSeminarSettlementDate memberSeminarSettlementDate
            SET memberSeminarSettlementDate.date = :targetDate
            WHERE memberSeminarSettlementDate.memberSeminar.id = :memberSeminarId
            AND NOT EXISTS (
                SELECT 1
                FROM SettlementItem settlementItem
                JOIN settlementItem.settlement settlement
                WHERE settlementItem.memberSeminar = memberSeminarSettlementDate.memberSeminar
                AND settlement.deleted_at IS NULL
                AND settlementItem.deleted_at IS NULL
            )
            """)
    int updateDateByMemberSeminarIdIfNotSettled(
            @Param("memberSeminarId") Long memberSeminarId,
            @Param("targetDate") LocalDate targetDate);
}
