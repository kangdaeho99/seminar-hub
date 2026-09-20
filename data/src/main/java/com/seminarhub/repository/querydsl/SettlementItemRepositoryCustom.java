package com.seminarhub.repository.querydsl;

import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementItemRepositoryCustom {
    List<SettlementItem> search(SettlementItemSearchQuery query);
    Page<SettlementItem> search(SettlementItemSearchQuery query, Pageable pageable);
    List<SettlementItem> search(SettlementItemSearchQuery query, CursorRequest cursorRequest);
}
