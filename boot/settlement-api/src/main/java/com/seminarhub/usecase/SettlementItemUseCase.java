package com.seminarhub.usecase;

import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.domain.settlement.service.SettlementItemService;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementItemUseCase {

    private final SettlementItemService service;

    public List<SettlementItem> findAll(SettlementItemSearchQuery query) { return service.findAll(query); }
    public Page<SettlementItem> findPage(SettlementItemSearchQuery query, Pageable pageable) {
        return service.findAll(query, pageable);
    }
    public List<SettlementItem> findByCursor(SettlementItemSearchQuery query, CursorRequest request) {
        return service.findByCursor(query, request);
    }
}
