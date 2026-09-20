package com.seminarhub.flow;

import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.usecase.SettlementItemUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementItemFlow {
    private final SettlementItemUseCase useCase;
    public List<SettlementItem> findAll(SettlementItemSearchQuery query) { return useCase.findAll(query); }
    public Page<SettlementItem> findPage(SettlementItemSearchQuery query, Pageable pageable) {
        return useCase.findPage(query, pageable);
    }
    public List<SettlementItem> findByCursor(SettlementItemSearchQuery query, CursorRequest request) {
        return useCase.findByCursor(query, request);
    }
}
