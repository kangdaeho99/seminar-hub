package com.seminarhub.domain.settlement.service;

import com.seminarhub.entity.SettlementItem;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.repository.SettlementItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementItemService {

    private final SettlementItemRepository repository;

    public List<SettlementItem> findAll(SettlementItemSearchQuery query) {
        return repository.search(query);
    }

    public Page<SettlementItem> findAll(SettlementItemSearchQuery query, Pageable pageable) {
        return repository.search(query, pageable);
    }

    public List<SettlementItem> findByCursor(SettlementItemSearchQuery query, CursorRequest cursorRequest) {
        return repository.search(query, cursorRequest);
    }
}
