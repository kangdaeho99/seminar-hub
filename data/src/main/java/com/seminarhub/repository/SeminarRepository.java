package com.seminarhub.repository;

import com.seminarhub.entity.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarRepository extends JpaRepository<Seminar, Long> {
}
