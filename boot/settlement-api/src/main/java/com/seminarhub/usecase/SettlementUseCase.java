package com.seminarhub.usecase;

import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.domain.settlement.service.SettlementService;
import com.seminarhub.dto.SettlementCreateRequest;
import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.dto.SettlementUpdateRequest;
import com.seminarhub.entity.Settlement;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.global.dto.CursorRequest;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public Settlement create(SettlementCreateRequest request) {
        return settlementService.save(Settlement.builder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .amount(request.amount())
                .settlement_status(request.settlementStatus())
                .build());
    }

    public Settlement read(Long id) {
        return settlementService
                .findById(id)
                .orElseThrow(() -> new BadRequestException("정산 정보를 찾을 수 없습니다. id=" + id));
    }

    public List<Settlement> readAll(List<Long> ids) {
        List<Long> distinctIds = new LinkedHashSet<>(ids).stream().toList();
        Map<Long, Settlement> settlementsById = settlementService.findByIds(distinctIds).stream()
                .collect(Collectors.toMap(Settlement::getId, Function.identity()));
        return distinctIds.stream().map(settlementsById::get).filter(Objects::nonNull).toList();
    }

    public Settlement update(Long id, SettlementUpdateRequest request) {
        Settlement settlement = read(id);
        LocalDate startDate = request.startDate() != null ? request.startDate() : settlement.getStartDate();
        LocalDate endDate = request.endDate() != null ? request.endDate() : settlement.getEndDate();
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("정산 시작일은 종료일보다 늦을 수 없습니다.");
        }
        return settlementService.update(
                settlement,
                request.startDate(),
                request.endDate(),
                request.amount(),
                request.settlementStatus());
    }

    public Settlement delete(Long id) {
        return settlementService.delete(read(id));
    }

    public List<Settlement> deleteAll(List<Long> ids) {
        return settlementService.deleteAll(readAll(ids));
    }

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
