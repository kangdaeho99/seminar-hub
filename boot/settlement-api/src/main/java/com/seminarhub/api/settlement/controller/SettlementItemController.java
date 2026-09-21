package com.seminarhub.api.settlement.controller;

import com.seminarhub.dto.ApiResponse;
import com.seminarhub.global.dto.CursorResponse;
import com.seminarhub.global.dto.Pagination;
import com.seminarhub.api.settlement.dto.SettlementItemResponse;
import com.seminarhub.api.settlement.dto.SettlementItemSearchRequest;
import com.seminarhub.api.settlement.facade.SettlementItemFacade;
import com.seminarhub.global.annotation.CursorDefault;
import com.seminarhub.global.dto.CursorRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settlement/items")
@Validated
public class SettlementItemController {

    private final SettlementItemFacade facade;

    @GetMapping("/list")
    public ApiResponse<List<SettlementItemResponse>> findAll(@Valid SettlementItemSearchRequest request) {
        return ApiResponse.ok(facade.findAll(request));
    }

    @GetMapping("/page")
    public ApiResponse<Pagination<SettlementItemResponse>> findPage(
            @Valid SettlementItemSearchRequest request,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.ok(facade.findPage(request, pageable));
    }

    @GetMapping("/cursor")
    public ApiResponse<CursorResponse<SettlementItemResponse>> findByCursor(
            @Valid SettlementItemSearchRequest request, @CursorDefault CursorRequest cursorRequest) {
        return ApiResponse.ok(facade.findByCursor(request, cursorRequest));
    }
}
