package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminarSettlementDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SettlementAggregationRepository extends JpaRepository<MemberSeminarSettlementDate, Long> {

    @Query(value = """
            SELECT ms.id AS memberSeminarId,
                   s.price AS price,
                   mssd.id AS settlementDateId
            FROM member_seminar_settlement_date mssd
            JOIN member_seminar ms ON mssd.member_seminar_id = ms.id
            JOIN seminar s ON ms.seminar_id = s.id
            WHERE mssd.date BETWEEN :startAt AND :endAt
              AND mssd.del_dt IS NULL
              AND ms.del_dt IS NULL
              AND s.del_dt IS NULL
              AND NOT EXISTS (
                    SELECT 1 FROM settlement_item si
                    WHERE si.member_seminar_id = ms.id 
                    AND si.deleted_at IS NULL
              )
            """, nativeQuery = true)
    java.util.List<com.seminarhub.dto.SettlementRecordProjection> findAggregateTarget(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt);

    @Query(value = """
            SELECT ms.id AS memberSeminarId,
                   s.price AS price,
                   mssd.id AS settlementDateId
            FROM member_seminar_settlement_date mssd
            JOIN member_seminar ms ON mssd.member_seminar_id = ms.id
            JOIN seminar s ON ms.seminar_id = s.id
            WHERE mssd.date BETWEEN :startAt AND :endAt
              AND mssd.del_dt IS NULL
              AND ms.del_dt IS NULL
              AND s.del_dt IS NULL
              AND NOT EXISTS (
                    SELECT 1 FROM settlement_item si
                    WHERE si.member_seminar_id = ms.id 
                    AND si.deleted_at IS NULL
              )
            FOR UPDATE OF mssd
            """, nativeQuery = true)
    java.util.List<com.seminarhub.dto.SettlementRecordProjection> findAggregateTargetForUpdate(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt);
}
