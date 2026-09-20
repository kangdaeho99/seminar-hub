package com.seminarhub.repository;

import com.seminarhub.dto.SettlementRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SettlementItemJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insertSettlementItems(Long settlementId, List<SettlementRecord> records) {
        String sql = "INSERT INTO settlement_item (settlement_id, member_seminar_id, amount, inst_dt, updt_dt) VALUES (?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        int batchSize = 500;

        jdbcTemplate.batchUpdate(sql, records, batchSize,
                (PreparedStatement ps, SettlementRecord record) -> {
                    ps.setLong(1, settlementId);
                    ps.setLong(2, record.memberSeminarId());
                    ps.setBigDecimal(3, BigDecimal.valueOf(record.price()));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                    ps.setTimestamp(5, Timestamp.valueOf(now));
                });
    }
}
