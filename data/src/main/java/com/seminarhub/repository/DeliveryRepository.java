package com.seminarhub.repository;

import com.seminarhub.entity.Delivery;
import com.seminarhub.entity.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from Delivery d join fetch d.memberSeminar where d.id = :id")
    Optional<Delivery> findByIdWithMemberSeminar(@Param("id") Long id);

    Optional<Delivery> findByMemberSeminar_Id(Long memberSeminarId);

    // RepositoryItemReader에서 PENDING 상태 배송 건을 페이징 조회할 때 사용
    Page<Delivery> findByDeliveryStatus(DeliveryStatus status, Pageable pageable);

    // 배치 파라미터(startAt, endAt) 기반 기간 조회 (inst_dt 참조)
    @Query("SELECT d FROM Delivery d WHERE d.inst_dt >= :startAt AND d.inst_dt <= :endAt")
    Page<Delivery> findByInstDtBetween(
            @Param("startAt") java.time.LocalDateTime startAt,
            @Param("endAt") java.time.LocalDateTime endAt,
            Pageable pageable);

    @Query("SELECT d FROM Delivery d WHERE d.deliveryStatus = :status AND d.inst_dt >= :startAt AND d.inst_dt <= :endAt")
    Page<Delivery> findByDeliveryStatusAndInstDtBetween(
            @Param("status") DeliveryStatus status,
            @Param("startAt") java.time.LocalDateTime startAt,
            @Param("endAt") java.time.LocalDateTime endAt,
            Pageable pageable);

    // 특정 배송 상태가 아닌 건을 기간 조건으로 조회
    @Query("""
            SELECT d
            FROM Delivery d
            WHERE d.deliveryStatus != :status
              AND d.inst_dt >= :startAt
              AND d.inst_dt <= :endAt
              AND d.id > :lastId
            ORDER BY d.id ASC
            """)
    List<Delivery> findByDeliveryStatusNotAndInstDtBetweenAndIdGreaterThan(
            @Param("status") DeliveryStatus status,
            @Param("startAt") java.time.LocalDateTime startAt,
            @Param("endAt") java.time.LocalDateTime endAt,
            @Param("lastId") Long lastId,
            Pageable pageable);
}
