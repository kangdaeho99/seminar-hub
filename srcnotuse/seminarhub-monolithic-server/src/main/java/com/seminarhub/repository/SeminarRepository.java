package com.seminarhub.repository;

import com.seminarhub.entity.GenericDTORowMapper;
import com.seminarhub.entity.SeminarDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SeminarRepository {
    private final JdbcTemplate jdbcTemplate;

    public SeminarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create
    public int insert(SeminarDTO seminar) {
        String sql = """
            INSERT INTO seminar (name, description, price, available_seats, reg_start_date, reg_end_date, 
                                 start_date, end_date, address, company_no, max_capacity, inst_dt, updt_dt)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return jdbcTemplate.update(sql,
                seminar.getName(),
                seminar.getDescription(),
                seminar.getPrice(),
                seminar.getAvailable_seats(),
                seminar.getReg_start_date(),
                seminar.getReg_end_date(),
                seminar.getStart_date(),
                seminar.getEnd_date(),
                seminar.getAddress(),
                seminar.getCompany_no(),
                seminar.getMax_capacity(),
                LocalDateTime.now(),  // inst_dt
                LocalDateTime.now()   // updt_dt
        );
    }
    // SELECT by seminar_no
    public SeminarDTO findSeminarById(long seminar_no) {
        String sql = "SELECT * FROM seminar WHERE seminar_no = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{seminar_no}, new GenericDTORowMapper<>(SeminarDTO.class));
    }

    public SeminarDTO findSeminarBySeminarNoWithPessimisticLock(long seminar_no){
        String sql = "SELECT * FROM seminar WHERE seminar_no = ? FOR UPDATE";
        return jdbcTemplate.queryForObject(sql, new Object[]{seminar_no}, new GenericDTORowMapper<>(SeminarDTO.class));
    }

    // SELECT List by company_no
    public List<SeminarDTO> findSeminarsByCompanyNo(int company_no) {
        String sql = "SELECT * FROM seminar WHERE company_no = ?";
        return jdbcTemplate.query(sql, new Object[]{company_no}, new GenericDTORowMapper<>(SeminarDTO.class));
    }
    // Update
    public int update(SeminarDTO seminar) {
        String sql = """
            UPDATE seminar
            SET name = ?, description = ?, price = ?, available_seats = ?, reg_start_date = ?, reg_end_date = ?, 
                start_date = ?, end_date = ?, address = ?, company_no = ?, max_capacity = ?, updt_dt = ?
            WHERE seminar_no = ?
        """;
        return jdbcTemplate.update(sql,
                seminar.getName(),
                seminar.getDescription(),
                seminar.getPrice(),
                seminar.getAvailable_seats(),
                seminar.getReg_start_date(),
                seminar.getReg_end_date(),
                seminar.getStart_date(),
                seminar.getEnd_date(),
                seminar.getAddress(),
                seminar.getCompany_no(),
                seminar.getMax_capacity(),
                LocalDateTime.now(),  // updt_dt
                seminar.getSeminar_no()
        );
    }
    public int decrementAvailableSeats(SeminarDTO seminar){
        String sql = """
                UPDATE seminar
                SET available_seats = available_seats - 1
                WHERE seminar_no = ?
                """;
        return jdbcTemplate.update(sql, seminar.getSeminar_no());
    }


    // Soft Delete
    public int softDelete(Long seminar_no) {
        String sql = "UPDATE seminar SET del_dt = ? WHERE seminar_no = ?";
        return jdbcTemplate.update(sql, LocalDateTime.now(), seminar_no);
    }

}