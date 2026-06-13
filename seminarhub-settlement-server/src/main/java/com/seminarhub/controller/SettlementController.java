package com.seminarhub.controller;

import com.seminarhub.dto.SettlementApiResponse;
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
    public SettlementApiResponse updateWithReadCommitted(@RequestBody SettlementDateUpdateRequest request) {
        settlementService.updateWithReadCommitted(request);
        return SettlementApiResponse.ok();
    }
    
    @GetMapping("/read-committed-pessimistic-write/aggregate")
    public SettlementApiResponse aggregateWithReadCommittedPessimisticWrite(@RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementService.aggregateWithReadCommittedPessimisticWrite(startAt, endAt);
        return SettlementApiResponse.ok();
    }

    @GetMapping("/repeatable-read/aggregate")
    public SettlementApiResponse aggregateWithRepeatableRead(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementService.aggregateWithRepeatableRead(startAt, endAt);
        return SettlementApiResponse.ok();
    }

    @GetMapping("/serializable/aggregate")
    public SettlementApiResponse aggregateWithSerializable(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt) {
        settlementService.aggregateWithSerializable(startAt, endAt);
        return SettlementApiResponse.ok();
    }

}
