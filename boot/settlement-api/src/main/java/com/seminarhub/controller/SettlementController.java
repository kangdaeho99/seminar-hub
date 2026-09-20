package com.seminarhub.controller;

import com.seminarhub.dto.ApiResponse;
import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settlement")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping("/read-committed/update")
    public ApiResponse<?> updateWithReadCommitted(@RequestBody SettlementDateUpdateRequest request) {
        settlementService.updateWithReadCommitted(request);
        return ApiResponse.ok();
    }
    
    @GetMapping("/read-committed-pessimistic-write/aggregate")
    public ApiResponse<?> aggregateWithReadCommittedPessimisticWrite(@RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementService.aggregateWithReadCommittedPessimisticWrite(startAt, endAt);
        return ApiResponse.ok();
    }

    @GetMapping("/repeatable-read/aggregate")
    public ApiResponse<?> aggregateWithRepeatableRead(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementService.aggregateWithRepeatableRead(startAt, endAt);
        return ApiResponse.ok();
    }
}
