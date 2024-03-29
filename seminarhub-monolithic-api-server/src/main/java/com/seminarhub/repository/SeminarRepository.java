package com.seminarhub.repository;

import com.seminarhub.entity.Seminar;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description: Repository interface for managing Seminar entities
 */
public interface SeminarRepository extends JpaRepository<Seminar, Long> {

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve a seminar by its seminar_name while ensuring it's not deleted (del_dt is null)
     */
    @Query("SELECT s FROM Seminar s WHERE s.seminar_name = :seminar_name AND del_dt is null")
    Optional<Seminar> findBySeminar_name(@Param("seminar_name") String seminar_name);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve a seminar by its seminar_name while ensuring it's not deleted (del_dt is null)
     */
    @Query("SELECT s FROM Seminar s WHERE s.seminar_no = :seminar_no AND del_dt is null")
    Optional<Seminar> findBySeminar_no(@Param("seminar_no") Long seminar_no);

    @Transactional
    @Modifying
    @Query("UPDATE Seminar s SET s.seminar_participants_cnt = s.seminar_participants_cnt + 1 WHERE s.seminar_no = :seminar_no AND del_dt is null")
    void incrementParticipantsCnt(@Param("seminar_no") Long seminar_no);

    @Transactional
    @Modifying
    @Query("UPDATE Seminar s SET s.seminar_participants_cnt = s.seminar_participants_cnt - 1 WHERE s.seminar_no = :seminar_no AND del_dt is null")
    void decreaseParticipantsCnt(@Param("seminar_no") Long seminar_no);

}
