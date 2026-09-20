package com.seminarhub.flow;

import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.entity.Settlement;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.usecase.SettlementUseCase;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementFlow {

    private final SettlementUseCase useCase;

    public List<Settlement> findAll(SettlementSearchQuery query) { return useCase.findAll(query); }
    public Page<Settlement> findPage(SettlementSearchQuery query, Pageable pageable) {
        return useCase.findPage(query, pageable);
    }
    public List<Settlement> findByCursor(SettlementSearchQuery query, CursorRequest request) {
        return useCase.findByCursor(query, request);
    }
    public void updateWithReadCommitted(SettlementDateUpdateRequest request) {
        useCase.updateWithReadCommitted(request);
    }
    public void aggregateWithReadCommittedPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        useCase.aggregateWithReadCommittedPessimisticWrite(startAt, endAt);
    }
    public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        useCase.aggregateWithRepeatableRead(startAt, endAt);
    }
}
