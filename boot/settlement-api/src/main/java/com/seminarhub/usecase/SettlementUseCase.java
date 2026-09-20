package com.seminarhub.usecase;

import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.domain.settlement.service.SettlementService;
import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.entity.Settlement;
import com.seminarhub.global.dto.CursorRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementUseCase {

    private final SettlementService settlementService;

    public List<Settlement> findAll(SettlementSearchQuery query) {
        return settlementService.findAll(query);
    }

    public Page<Settlement> findPage(SettlementSearchQuery query, Pageable pageable) {
        return settlementService.findAll(query, pageable);
    }

    public List<Settlement> findByCursor(SettlementSearchQuery query, CursorRequest cursorRequest) {
        return settlementService.findByCursor(query, cursorRequest);
    }

    public void updateWithReadCommitted(SettlementDateUpdateRequest request) {
        settlementService.updateWithReadCommitted(request.memberSeminarId(), request.targetDate());
    }

    public void aggregateWithReadCommittedPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        settlementService.aggregateWithReadCommittedPessimisticWrite(startAt, endAt);
    }

    @Retryable(retryFor = CannotAcquireLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        settlementService.aggregateWithRepeatableRead(startAt, endAt);
    }
}
