package com.seminarhub.repository;

import com.seminarhub.entity.Member_Seminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberSeminarRepository extends JpaRepository<Member_Seminar, Long> {

    @Query("SELECT m_s FROM Member_Seminar m_s WHERE m_s.member_seminar_no = :member_seminar_no AND del_dt is null")
    Optional<Member_Seminar> findByMember_Seminar_no(@Param("member_seminar_no") Long member_seminar_no);

    @Query("SELECT m_s FROM Member_Seminar m_s WHERE m_s.member.member_id = :member_id AND del_dt is null")
    List<Member_Seminar> findAllByMember_id(@Param("member_id") String member_id);

    @Query("SELECT m_s FROM Member_Seminar m_s WHERE m_s.seminar.seminar_name = :seminar_name AND del_dt is null")
    List<Member_Seminar> findAllBySeminar_name(@Param("seminar_name") String seminar_name);


}
