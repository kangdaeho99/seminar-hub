package com.seminarhub.domain.settlement.service;

import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.domain.SettlementItem;
import com.seminarhub.domain.settlement.repository.SettlementItemRepository;
import com.seminarhub.global.dto.CursorRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public SettlementItem save(SettlementItem item) {
        return repository.save(item);
    }

    public Optional<SettlementItem> findById(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id);
    }

    public List<SettlementItem> findByIds(List<Long> ids) {
        return repository.findAllByIdInAndDeletedAtIsNull(ids);
    }

    public List<SettlementItem> findAll(SettlementItemSearchQuery query) {
        return repository.search(query);
    }

    public Page<SettlementItem> findAll(SettlementItemSearchQuery query, Pageable pageable) {
        return repository.search(query, pageable);
    }

    public List<SettlementItem> findByCursor(SettlementItemSearchQuery query, CursorRequest cursorRequest) {
        return repository.search(query, cursorRequest);
    }

    @Transactional
    public SettlementItem update(
            SettlementItem item, Settlement settlement, MemberSeminar memberSeminar, BigDecimal amount) {
        item.update(settlement, memberSeminar, amount);
        return item;
    }

    @Transactional
    public SettlementItem delete(SettlementItem item) {
        item.delete();
        return item;
    }

    @Transactional
    public List<SettlementItem> deleteAll(List<SettlementItem> items) {
        items.forEach(SettlementItem::delete);
        return items;
    }
}
