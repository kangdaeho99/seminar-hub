package com.seminarhub.repository;

import com.seminarhub.entity.Settlement;
import com.seminarhub.repository.querydsl.SettlementRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettlementRepository extends JpaRepository<Settlement, Long>, SettlementRepositoryCustom {

    @Query("select s from Settlement s where s.id = :id and s.deleted_at is null")
    Optional<Settlement> findActiveById(@Param("id") Long id);

    @Query("select s from Settlement s where s.id in :ids and s.deleted_at is null")
    List<Settlement> findActiveByIds(@Param("ids") List<Long> ids);
}
