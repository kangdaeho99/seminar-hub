package com.seminarhub.domain.settlement.repository;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberSeminarSettlementDateJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void updateMemberSeminarSettlementDateToTriggerLock(List<SettlementRecord> records) {
        String sql = "UPDATE member_seminar_settlement_date SET date = date WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, records, 500,
                (PreparedStatement ps, SettlementRecord record) -> ps.setLong(1, record.settlementDateId()));
    }
}
