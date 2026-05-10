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
@RequestMapping("/api/v1/settlement")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping("/pessimistic/update")
    public SettlementApiResponse updateWithPessimisticLock(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithPessimisticLock(request);
    }

    @GetMapping("/pessimistic/aggregate")
    public SettlementApiResponse aggregateWithPessimisticLock(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithPessimisticLock(startAt, endAt);
    }

    @PostMapping("/read-committed/update")
    public SettlementApiResponse updateWithReadCommitted(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithReadCommitted(request);
    }

    @GetMapping("/read-committed/aggregate")
    public SettlementApiResponse aggregateWithReadCommitted(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithReadCommitted(startAt, endAt);
    }

    @PostMapping("/repeatable-read/update")
    public SettlementApiResponse updateWithRepeatableRead(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithRepeatableRead(request);
    }

    @GetMapping("/repeatable-read/aggregate")
    public SettlementApiResponse aggregateWithRepeatableRead(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithRepeatableRead(startAt, endAt);
    }

    @PostMapping("/serializable/update")
    public SettlementApiResponse updateWithSerializable(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithSerializable(request);
    }

    @GetMapping("/serializable/aggregate")
    public SettlementApiResponse aggregateWithSerializable(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithSerializable(startAt, endAt);
    }
}
