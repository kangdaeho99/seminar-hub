package com.seminarhub.domain.settlement.repository;

import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.repository.querydsl.SettlementRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long>, SettlementRepositoryCustom {
    Optional<Settlement> findByIdAndDeletedAtIsNull(Long id);
    List<Settlement> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
