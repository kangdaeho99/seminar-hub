package com.seminarhub.repository;

import com.seminarhub.entity.GenericDTORowMapper;
import com.seminarhub.entity.MemberDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create
    public int Insert(MemberDTO member) {
        String sql = """
            INSERT INTO member (id, name, pw, gender, age, address, job, grade, points, inst_dt, updt_dt) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return jdbcTemplate.update(sql,
                member.getId(),
                member.getName(),
                member.getPw(),
                member.getGender(),
                member.getAge(),
                member.getAddress(),
                member.getJob(),
                member.getGrade(),
                member.getPoints(),
                LocalDateTime.now(),  // inst_dt
                LocalDateTime.now()   // updt_dt
        );
    }

    //SELECT
    public MemberDTO findMemberByMemberNo(Long member_no) {
        String sql = "SELECT * FROM member WHERE member_no = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{member_no}, new GenericDTORowMapper<>(MemberDTO.class));
    }

    public MemberDTO findMemberById(String id){
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new GenericDTORowMapper<>(MemberDTO.class));
    }

    //SELECT LIST
    public List<MemberDTO> findMemberByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";
        return jdbcTemplate.query(sql, new Object[]{name}, new GenericDTORowMapper<>(MemberDTO.class));
    }


    //Update
    public int update(MemberDTO member) {
        String sql = """
            UPDATE member 
            SET id = ?, name = ?, pw = ?, gender = ?, age = ?, address = ?, job = ?, grade = ?, points = ?, updt_dt = ?
            WHERE member_no = ?
        """;
        return jdbcTemplate.update(sql,
                member.getId(),
                member.getName(),
                member.getPw(),
                member.getGender(),
                member.getAge(),
                member.getAddress(),
                member.getJob(),
                member.getGrade(),
                member.getPoints(),
                LocalDateTime.now(),  // updt_dt
                member.getMember_no()
        );
    }

    //Delete
    public int softDelete(Long memberNo) {
        String sql = "UPDATE member SET del_dt = ? WHERE member_no = ?";
        return jdbcTemplate.update(sql, LocalDateTime.now(), memberNo);
    }


    public MemberDTO findPwById(String id){
        String sql = "SELECT pw FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{id}, new GenericDTORowMapper<>(MemberDTO.class));
    }
}
