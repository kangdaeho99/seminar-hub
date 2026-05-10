package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminarSettlementDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SettlementAggregationRepository extends JpaRepository<MemberSeminarSettlementDate, Long> {

    @Query("""
            select coalesce(sum(seminar.seminar_price), 0)
            from MemberSeminarSettlementDate settlementDate
            join settlementDate.memberSeminar memberSeminar
            join memberSeminar.seminar seminar
            where settlementDate.date between :startAt and :endAt
            and memberSeminar.del_dt is null
            and seminar.del_dt is null
            """)
    Long sumSeminarPriceBySettlementDateBetween(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt
    );
}
