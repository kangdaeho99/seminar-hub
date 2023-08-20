package com.seminarhub.repository;

import com.seminarhub.entity.Seminar;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeminarRepository extends JpaRepository<Seminar, Long> {

    @Query("SELECT s FROM Seminar s WHERE s.seminar_name = :seminar_name AND del_dt is null")
    Optional<Seminar> findBySeminar_name(String seminar_name);

    @Transactional
    @Modifying
    @Query("UPDATE Seminar s SET s.del_dt = CURRENT_TIMESTAMP WHERE s.seminar_name = :seminar_name")
    void deleteBySeminar_name(String seminar_name);

}
