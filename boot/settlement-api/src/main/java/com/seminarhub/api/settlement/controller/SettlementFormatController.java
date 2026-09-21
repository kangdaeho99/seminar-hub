package com.seminarhub.api.settlement.controller;

import com.seminarhub.dto.ApiResponse;
import com.seminarhub.global.dto.CursorResponse;
import com.seminarhub.global.dto.Pagination;
import com.seminarhub.api.settlement.dto.SettlementCreateRequest;
import com.seminarhub.api.settlement.dto.SettlementResponse;
import com.seminarhub.api.settlement.dto.SettlementSearchRequest;
import com.seminarhub.api.settlement.dto.SettlementUpdateRequest;
import com.seminarhub.api.settlement.facade.SettlementFacade;
import com.seminarhub.global.annotation.CursorDefault;
import com.seminarhub.global.annotation.CursorSwagger;
import com.seminarhub.global.annotation.PageableSwagger;
import com.seminarhub.global.dto.CursorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SettlementFormat", description = "정산 포맷 API")
@RestController
@RequestMapping("/settlement-format")
@RequiredArgsConstructor
@Validated
public class SettlementFormatController {

    private final SettlementFacade settlementFacade;

    @Operation(operationId = "settlement-format-01-create", summary = "정산 생성", description = "정산 정보를 생성합니다.")
    @PostMapping
    public ApiResponse<SettlementResponse> create(@Valid @RequestBody SettlementCreateRequest request) {
        return ApiResponse.created(settlementFacade.create(request));
    }

    @Operation(
            operationId = "settlement-format-02-update",
            summary = "정산 수정",
            description = "전달된 정산 기간, 금액, 상태를 수정합니다.")
    @PatchMapping("/{id}")
    public ApiResponse<SettlementResponse> update(
            @Parameter(description = "정산 ID", example = "1024", required = true)
                    @Positive(message = "정산 ID는 양수여야 합니다.")
                    @PathVariable
                    Long id,
            @Valid @RequestBody SettlementUpdateRequest request) {
        return ApiResponse.ok(settlementFacade.update(id, request));
    }

    @Operation(
            operationId = "settlement-format-03-read",
            summary = "정산 단건 조회",
            description = "ID로 삭제되지 않은 정산 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<SettlementResponse> read(
            @Parameter(description = "정산 ID", example = "1024", required = true)
                    @Positive(message = "정산 ID는 양수여야 합니다.")
                    @PathVariable
                    Long id) {
        return ApiResponse.ok(settlementFacade.read(id));
    }

    @Operation(
            operationId = "settlement-format-04-delete",
            summary = "정산 단건 삭제",
            description = "삭제 일시를 기록하는 Soft Delete를 수행하고 삭제 상태를 반환합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<SettlementResponse> delete(
            @Parameter(description = "정산 ID", example = "1024", required = true)
                    @Positive(message = "정산 ID는 양수여야 합니다.")
                    @PathVariable
                    Long id) {
        return ApiResponse.ok(settlementFacade.delete(id));
    }

    @Operation(
            operationId = "settlement-format-05-read-batch",
            summary = "정산 ID 목록 조회",
            description = "유효한 활성 ID만 첫 등장 순서로 조회합니다.")
    @GetMapping("/batch")
    public ApiResponse<List<SettlementResponse>> readAll(
            @Parameter(description = "정산 ID 목록", example = "1024,1025", required = true)
                    @NotEmpty(message = "정산 ID 목록은 비어 있을 수 없습니다.")
                    @RequestParam
                    List<@Positive(message = "정산 ID는 양수여야 합니다.") Long> ids) {
        return ApiResponse.ok(settlementFacade.readAll(ids));
    }

    @Operation(
            operationId = "settlement-format-06-delete-batch",
            summary = "정산 ID 목록 삭제",
            description = "유효한 활성 ID만 Soft Delete하고 첫 등장 입력 순서로 반환합니다.")
    @DeleteMapping("/batch")
    public ApiResponse<List<SettlementResponse>> deleteAll(
            @Parameter(description = "정산 ID 목록", example = "1024,1025", required = true)
                    @NotEmpty(message = "정산 ID 목록은 비어 있을 수 없습니다.")
                    @RequestParam
                    List<@Positive(message = "정산 ID는 양수여야 합니다.") Long> ids) {
        return ApiResponse.ok(settlementFacade.deleteAll(ids));
    }

    @Operation(
            operationId = "settlement-format-07-list",
            summary = "정산 목록 조회",
            description = "검색 조건에 맞는 정산 전체를 ID 내림차순으로 조회합니다.")
    @GetMapping("/list")
    public ApiResponse<List<SettlementResponse>> findAll(
            @Valid @ParameterObject SettlementSearchRequest request) {
        return ApiResponse.ok(settlementFacade.findAll(request));
    }

    @Operation(
            operationId = "settlement-format-08-page",
            summary = "정산 페이지 조회",
            description = "검색 조건에 맞는 정산을 페이지 단위로 조회합니다.")
    @PageableSwagger
    @GetMapping("/page")
    public ApiResponse<Pagination<SettlementResponse>> findPage(
            @Valid @ParameterObject SettlementSearchRequest request,
            @Parameter(hidden = true) @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.ok(settlementFacade.findPage(request, pageable));
    }

    @Operation(
            operationId = "settlement-format-09-cursor",
            summary = "정산 커서 조회",
            description = "검색 조건에 맞는 정산을 ID 커서 방식으로 조회합니다.")
    @CursorSwagger
    @GetMapping("/cursor")
    public ApiResponse<CursorResponse<SettlementResponse>> findByCursor(
            @Valid @ParameterObject SettlementSearchRequest request,
            @Parameter(hidden = true) @CursorDefault CursorRequest cursorRequest) {
        return ApiResponse.ok(settlementFacade.findByCursor(request, cursorRequest));
    }
}
