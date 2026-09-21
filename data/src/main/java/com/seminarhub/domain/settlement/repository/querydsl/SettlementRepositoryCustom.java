package com.seminarhub.domain.settlement.repository.querydsl;

import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementRepositoryCustom {
    List<Settlement> search(SettlementSearchQuery query);
    Page<Settlement> search(SettlementSearchQuery query, Pageable pageable);
    List<Settlement> search(SettlementSearchQuery query, CursorRequest cursorRequest);
}
