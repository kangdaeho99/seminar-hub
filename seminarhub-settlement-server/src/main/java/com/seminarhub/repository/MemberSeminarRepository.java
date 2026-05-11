package com.seminarhub.repository;

import com.seminarhub.entity.Member_Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSeminarRepository extends JpaRepository<Member_Seminar, Long> {
}
