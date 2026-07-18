package com.seminarhub.repository;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from Delivery d join fetch d.memberSeminar where d.id = :id")
    Optional<Delivery> findByIdWithMemberSeminar(@Param("id") Long id);

    Optional<Delivery> findByMemberSeminar_Id(Long memberSeminarId);

    // RepositoryItemReader에서 PENDING 상태 배송 건을 페이징 조회할 때 사용
    Page<Delivery> findByDeliveryStatus(DeliveryStatus status, Pageable pageable);
}
