package com.seminarhub.repository;

import com.seminarhub.dto.SettlementRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberSeminarSettlementDateJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void updateMemberSeminarSettlementDateToTriggerLock(List<SettlementRecord> records) {
        String updateMssdSql = "UPDATE member_seminar_settlement_date SET date = date WHERE id = ?";
        int batchSize = 500;

        jdbcTemplate.batchUpdate(updateMssdSql, records, batchSize,
                (PreparedStatement ps, SettlementRecord record) -> {
                    ps.setLong(1, record.settlementDateId());
                });
    }
}
