package com.seminarhub.facade;

import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.dto.CursorResponse;
import com.seminarhub.dto.Pagination;
import com.seminarhub.dto.SettlementItemResponse;
import com.seminarhub.dto.SettlementItemSearchRequest;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.flow.SettlementItemFlow;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementItemFacade {

    private final SettlementItemFlow flow;

    public List<SettlementItemResponse> findAll(SettlementItemSearchRequest request) {
        return toResponses(flow.findAll(toQuery(request)));
    }

    public Pagination<SettlementItemResponse> findPage(SettlementItemSearchRequest request, Pageable pageable) {
        Page<SettlementItemResponse> page = flow.findPage(toQuery(request), pageable).map(SettlementItemResponse::from);
        return Pagination.of(page);
    }

    public CursorResponse<SettlementItemResponse> findByCursor(
            SettlementItemSearchRequest request, CursorRequest cursorRequest) {
        List<SettlementItemResponse> responses = toResponses(flow.findByCursor(toQuery(request), cursorRequest));
        return CursorResponse.of(responses, cursorRequest.getSize(), SettlementItemResponse::id);
    }

    private List<SettlementItemResponse> toResponses(List<SettlementItem> items) {
        return items.stream().map(SettlementItemResponse::from).toList();
    }

    private SettlementItemSearchQuery toQuery(SettlementItemSearchRequest r) {
        return new SettlementItemSearchQuery(
                r.id(), r.ids(), r.settlementId(), r.settlementIds(), r.memberSeminarId(), r.memberSeminarIds(),
                r.amount(), r.amountMin(), r.amountMax(), r.createdAt(), r.updatedAt(), r.deletedAt(),
                r.createdStartAt(), r.createdEndAt(), r.updatedStartAt(), r.updatedEndAt(), r.deletedStartAt(),
                r.deletedEndAt());
    }
}
