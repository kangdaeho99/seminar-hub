package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminarSettlementDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SettlementAggregationRepository extends JpaRepository<MemberSeminarSettlementDate, Long> {


    @Query("""
            select new com.seminarhub.dto.SettlementRecord(
                memberSeminar.member_seminar_no,
                seminar.seminar_price,
                settlementDate.id
            )
            from MemberSeminarSettlementDate settlementDate
            join settlementDate.memberSeminar memberSeminar
            join memberSeminar.seminar seminar
            where settlementDate.date between :startAt and :endAt
            and memberSeminar.del_dt is null
            """)
    java.util.List<com.seminarhub.dto.SettlementRecord> findAllBySettlementDateBetween(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt
    );
}
