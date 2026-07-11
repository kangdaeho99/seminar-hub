package com.seminarhub.repository;

import com.seminarhub.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    
    @Query("select d from Delivery d join fetch d.memberSeminar where d.id = :id")
    Optional<Delivery> findByIdWithMemberSeminar(@Param("id") Long id);

    Optional<Delivery> findByMemberSeminar_Id(Long memberSeminarId);
}
