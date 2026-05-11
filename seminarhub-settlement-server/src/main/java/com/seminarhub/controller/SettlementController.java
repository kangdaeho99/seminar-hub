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

    // curl -X POST "http://localhost:8080/api/v1/settlement/pessimistic/update" -H "Content-Type: application/json" -d '{"settlementDateId": 1, "targetDate": "2025-12-31"}'
    @PostMapping("/pessimistic/update")
    public SettlementApiResponse updateWithPessimisticLock(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithPessimisticLock(request);
    }

    // curl -X GET "http://localhost:8080/api/v1/settlement/pessimistic/aggregate?startAt=2025-01-01&endAt=2025-01-31"
    @GetMapping("/pessimistic/aggregate")
    public SettlementApiResponse aggregateWithPessimisticLock(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithPessimisticLock(startAt, endAt);
    }

    // curl.exe -X POST "http://localhost:8080/api/v1/settlement/read-committed/update" ^
    //   -H "Content-Type: application/json" ^
    //  -d "{\"settlementDateId\": 1, \"targetDate\": \"2025-12-31\"}"
    @PostMapping("/read-committed/update")
    public SettlementApiResponse updateWithReadCommitted(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithReadCommitted(request);
    }

    // curl.exe -X GET "http://localhost:8080/api/v1/settlement/read-committed/aggregate?startAt=2025-01-01&endAt=2025-01-31"
    @GetMapping("/read-committed/aggregate")
    public SettlementApiResponse aggregateWithReadCommitted(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithReadCommitted(startAt, endAt);
    }

    // curl.exe -X POST "http://localhost:8080/api/v1/settlement/repeatable-read/update" ^
    //   -H "Content-Type: application/json" ^
    //  -d "{\"settlementDateId\": 1, \"targetDate\": \"2025-12-31\"}"
    @PostMapping("/repeatable-read/update")
    public SettlementApiResponse updateWithRepeatableRead(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithRepeatableRead(request);
    }

    // curl.exe -X GET "http://localhost:8080/api/v1/settlement/repeatable-read/aggregate?startAt=2025-01-01&endAt=2025-01-31"
    @GetMapping("/repeatable-read/aggregate")
    public SettlementApiResponse aggregateWithRepeatableRead(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithRepeatableRead(startAt, endAt);
    }

    // curl.exe -X POST "http://localhost:8080/api/v1/settlement/serializable/update" ^
    //   -H "Content-Type: application/json" ^
    //  -d "{\"settlementDateId\": 1, \"targetDate\": \"2025-12-31\"}"
    @PostMapping("/serializable/update")
    public SettlementApiResponse updateWithSerializable(@RequestBody SettlementDateUpdateRequest request) {
        return settlementService.updateWithSerializable(request);
    }

    // curl.exe -X GET "http://localhost:8080/api/v1/settlement/serializable/aggregate?startAt=2025-01-01&endAt=2025-01-31"
    @GetMapping("/serializable/aggregate")
    public SettlementApiResponse aggregateWithSerializable(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt
    ) {
        return settlementService.aggregateWithSerializable(startAt, endAt);
    }
}
