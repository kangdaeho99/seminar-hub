package com.seminarhub.domain.delivery.repository;
import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.repository.querydsl.DeliveryRepositoryCustom;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface DeliveryRepository extends JpaRepository<Delivery, Long>, DeliveryRepositoryCustom {
    Optional<Delivery> findByIdAndDeletedAtIsNull(Long id);
    List<Delivery> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
    @Query("select d from Delivery d join fetch d.memberSeminar where d.id = :id")
    Optional<Delivery> findByIdWithMemberSeminar(@Param("id") Long id);
    Optional<Delivery> findByMemberSeminar_Id(Long memberSeminarId);
    Page<Delivery> findByDeliveryStatus(DeliveryStatus status, Pageable pageable);
    @Query("SELECT d FROM Delivery d WHERE d.createdAt >= :startAt AND d.createdAt <= :endAt")
    Page<Delivery> findByInstDtBetween(@Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt, Pageable pageable);
    @Query("SELECT d FROM Delivery d WHERE d.deliveryStatus = :status AND d.createdAt >= :startAt AND d.createdAt <= :endAt")
    Page<Delivery> findByDeliveryStatusAndInstDtBetween(@Param("status") DeliveryStatus status,
            @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt, Pageable pageable);
    @Query("""
            SELECT d FROM Delivery d WHERE d.deliveryStatus != :status
            AND d.createdAt >= :startAt AND d.createdAt <= :endAt AND d.id > :lastId ORDER BY d.id ASC""")
    List<Delivery> findByDeliveryStatusNotAndInstDtBetweenAndIdGreaterThan(@Param("status") DeliveryStatus status,
            @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt,
            @Param("lastId") Long lastId, Pageable pageable);
}
