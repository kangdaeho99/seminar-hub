package com.seminarhub.repository;

import com.seminarhub.entity.Settlement;
import com.seminarhub.repository.querydsl.SettlementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long>, SettlementRepositoryCustom {
}
