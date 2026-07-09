package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSeminarRepository extends JpaRepository<MemberSeminar, Long> {
}
