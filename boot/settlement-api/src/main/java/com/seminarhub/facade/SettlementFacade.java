package com.seminarhub.facade;

import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.dto.CursorResponse;
import com.seminarhub.dto.Pagination;
import com.seminarhub.dto.SettlementCreateRequest;
import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.dto.SettlementResponse;
import com.seminarhub.dto.SettlementSearchRequest;
import com.seminarhub.dto.SettlementUpdateRequest;
import com.seminarhub.entity.Settlement;
import com.seminarhub.flow.SettlementFlow;
import com.seminarhub.global.dto.CursorRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementFacade {

    private final SettlementFlow flow;

    public SettlementResponse create(SettlementCreateRequest request) {
        return SettlementResponse.from(flow.create(request));
    }

    public SettlementResponse read(Long id) {
        return SettlementResponse.from(flow.read(id));
    }

    public List<SettlementResponse> readAll(List<Long> ids) {
        return toResponses(flow.readAll(ids));
    }

    public SettlementResponse update(Long id, SettlementUpdateRequest request) {
        return SettlementResponse.from(flow.update(id, request));
    }

    public SettlementResponse delete(Long id) {
        return SettlementResponse.from(flow.delete(id));
    }

    public List<SettlementResponse> deleteAll(List<Long> ids) {
        return toResponses(flow.deleteAll(ids));
    }

    public List<SettlementResponse> findAll(SettlementSearchRequest request) {
        return toResponses(flow.findAll(toQuery(request)));
    }

    public Pagination<SettlementResponse> findPage(SettlementSearchRequest request, Pageable pageable) {
        Page<SettlementResponse> page = flow.findPage(toQuery(request), pageable).map(SettlementResponse::from);
        return Pagination.of(page);
    }

    public CursorResponse<SettlementResponse> findByCursor(
            SettlementSearchRequest request, CursorRequest cursorRequest) {
        List<SettlementResponse> responses = toResponses(flow.findByCursor(toQuery(request), cursorRequest));
        return CursorResponse.of(responses, cursorRequest.getSize(), SettlementResponse::id);
    }

    public void updateWithReadCommitted(SettlementDateUpdateRequest request) {
        flow.updateWithReadCommitted(request);
    }

    public void aggregateWithReadCommittedPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        flow.aggregateWithReadCommittedPessimisticWrite(startAt, endAt);
    }

    public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        flow.aggregateWithRepeatableRead(startAt, endAt);
    }

    private List<SettlementResponse> toResponses(List<Settlement> settlements) {
        return settlements.stream().map(SettlementResponse::from).toList();
    }

    private SettlementSearchQuery toQuery(SettlementSearchRequest r) {
        return new SettlementSearchQuery(
                r.id(), r.ids(), r.startDate(), r.startDateFrom(), r.startDateTo(), r.endDate(), r.endDateFrom(),
                r.endDateTo(), r.amount(), r.amountMin(), r.amountMax(), r.settlementStatus(),
                r.settlementStatuses(), r.createdAt(), r.updatedAt(), r.deletedAt(), r.createdStartAt(),
                r.createdEndAt(), r.updatedStartAt(), r.updatedEndAt(), r.deletedStartAt(), r.deletedEndAt());
    }
}
