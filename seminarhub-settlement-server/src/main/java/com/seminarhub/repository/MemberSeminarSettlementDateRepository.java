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
