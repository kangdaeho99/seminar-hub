package com.seminarhub.repository;

import com.seminarhub.entity.SettlementItem;
import com.seminarhub.repository.querydsl.SettlementItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementItemRepository extends JpaRepository<SettlementItem, Long>, SettlementItemRepositoryCustom {
}
