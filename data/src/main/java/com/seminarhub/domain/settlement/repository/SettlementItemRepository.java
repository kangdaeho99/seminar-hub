package com.seminarhub.domain.settlement.repository;

import com.seminarhub.domain.settlement.domain.SettlementItem;
import com.seminarhub.domain.settlement.repository.querydsl.SettlementItemRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementItemRepository extends JpaRepository<SettlementItem, Long>, SettlementItemRepositoryCustom {
    Optional<SettlementItem> findByIdAndDeletedAtIsNull(Long id);
    List<SettlementItem> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
