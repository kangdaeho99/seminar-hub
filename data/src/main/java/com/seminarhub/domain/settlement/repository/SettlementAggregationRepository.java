package com.seminarhub.domain.settlement.repository;

import com.seminarhub.domain.seminar.domain.MemberSeminarSettlementDate;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettlementAggregationRepository extends JpaRepository<MemberSeminarSettlementDate, Long> {
    @Query(value = """
            SELECT ms.id AS memberSeminarId, s.price AS price, mssd.id AS settlementDateId
            FROM member_seminar_settlement_date mssd
            JOIN member_seminar ms ON mssd.member_seminar_id = ms.id
            JOIN seminar s ON ms.seminar_id = s.id
            WHERE mssd.date BETWEEN :startAt AND :endAt
              AND mssd.deleted_at IS NULL AND ms.deleted_at IS NULL AND s.deleted_at IS NULL
              AND NOT EXISTS (SELECT 1 FROM settlement_item si
                              WHERE si.member_seminar_id = ms.id AND si.deleted_at IS NULL)
            """, nativeQuery = true)
    List<SettlementRecordProjection> findAggregateTarget(
            @Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);

    @Query(value = """
            SELECT ms.id AS memberSeminarId, s.price AS price, mssd.id AS settlementDateId
            FROM member_seminar_settlement_date mssd
            JOIN member_seminar ms ON mssd.member_seminar_id = ms.id
            JOIN seminar s ON ms.seminar_id = s.id
            WHERE mssd.date BETWEEN :startAt AND :endAt
              AND mssd.deleted_at IS NULL AND ms.deleted_at IS NULL AND s.deleted_at IS NULL
              AND NOT EXISTS (SELECT 1 FROM settlement_item si
                              WHERE si.member_seminar_id = ms.id AND si.deleted_at IS NULL)
            FOR NO KEY UPDATE OF mssd
            """, nativeQuery = true)
    List<SettlementRecordProjection> findAggregateTargetForUpdate(
            @Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);
}
