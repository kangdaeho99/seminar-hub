package com.seminarhub.api.settlement.controller;

import com.seminarhub.dto.ApiResponse;
import com.seminarhub.global.dto.CursorResponse;
import com.seminarhub.global.dto.Pagination;
import com.seminarhub.api.settlement.dto.SettlementDateUpdateRequest;
import com.seminarhub.api.settlement.dto.SettlementResponse;
import com.seminarhub.api.settlement.dto.SettlementSearchRequest;
import com.seminarhub.api.settlement.facade.SettlementFacade;
import com.seminarhub.global.annotation.CursorDefault;
import com.seminarhub.global.dto.CursorRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settlement")
@Validated
public class SettlementController {

    private final SettlementFacade settlementFacade;

    @GetMapping("/list")
    public ApiResponse<List<SettlementResponse>> findAll(@Valid SettlementSearchRequest request) {
        return ApiResponse.ok(settlementFacade.findAll(request));
    }

    @GetMapping("/page")
    public ApiResponse<Pagination<SettlementResponse>> findPage(
            @Valid SettlementSearchRequest request,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.ok(settlementFacade.findPage(request, pageable));
    }

    @GetMapping("/cursor")
    public ApiResponse<CursorResponse<SettlementResponse>> findByCursor(
            @Valid SettlementSearchRequest request, @CursorDefault CursorRequest cursorRequest) {
        return ApiResponse.ok(settlementFacade.findByCursor(request, cursorRequest));
    }

    @PostMapping("/read-committed/update")
    public ApiResponse<?> updateWithReadCommitted(@RequestBody SettlementDateUpdateRequest request) {
        settlementFacade.updateWithReadCommitted(request);
        return ApiResponse.ok();
    }
    
    @GetMapping("/read-committed-pessimistic-write/aggregate")
    public ApiResponse<?> aggregateWithReadCommittedPessimisticWrite(@RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementFacade.aggregateWithReadCommittedPessimisticWrite(startAt, endAt);
        return ApiResponse.ok();
    }

    @GetMapping("/repeatable-read/aggregate")
    public ApiResponse<?> aggregateWithRepeatableRead(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementFacade.aggregateWithRepeatableRead(startAt, endAt);
        return ApiResponse.ok();
    }
}
