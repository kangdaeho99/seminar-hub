package com.seminarhub.api.settlement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seminarhub.global.dto.CursorResponse;
import com.seminarhub.global.dto.Pagination;
import com.seminarhub.api.settlement.dto.SettlementResponse;
import com.seminarhub.domain.settlement.enums.SettlementStatus;
import com.seminarhub.global.error.SettlementExceptionHandler;
import com.seminarhub.api.settlement.facade.SettlementFacade;
import com.seminarhub.global.resolver.CursorRequestArgumentResolver;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SettlementFormatControllerTest {

    private SettlementFacade facade;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        facade = mock(SettlementFacade.class);
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setOneIndexedParameters(true);
        mockMvc = MockMvcBuilders.standaloneSetup(new SettlementFormatController(facade))
                .setCustomArgumentResolvers(new CursorRequestArgumentResolver(), pageableResolver)
                .setControllerAdvice(new SettlementExceptionHandler())
                .build();
    }

    @Test
    void exposesCreateUpdateReadAndDeleteContracts() throws Exception {
        SettlementResponse response = response(1L);
        when(facade.create(any())).thenReturn(response);
        when(facade.update(anyLong(), any())).thenReturn(response);
        when(facade.read(1L)).thenReturn(response);
        when(facade.delete(1L)).thenReturn(response);

        mockMvc.perform(post("/settlement-format")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(patch("/settlement-format/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":200000,"settlementStatus":"COMPLETED"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(get("/settlement-format/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(delete("/settlement-format/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void exposesBatchListPageAndCursorContracts() throws Exception {
        SettlementResponse response = response(1L);
        List<SettlementResponse> responses = List.of(response);
        when(facade.readAll(any())).thenReturn(responses);
        when(facade.deleteAll(any())).thenReturn(responses);
        when(facade.findAll(any())).thenReturn(responses);
        when(facade.findPage(any(), any())).thenReturn(Pagination.of(
                new PageImpl<>(responses, PageRequest.of(0, 20), 1)));
        when(facade.findByCursor(any(), any()))
                .thenReturn(CursorResponse.of(responses, 10, SettlementResponse::id));

        mockMvc.perform(get("/settlement-format/batch").param("ids", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
        mockMvc.perform(delete("/settlement-format/batch").param("ids", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
        mockMvc.perform(get("/settlement-format/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
        mockMvc.perform(get("/settlement-format/page").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pagination.currentPage").value(1));
        mockMvc.perform(get("/settlement-format/cursor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cursor.hasNext").value(false));
    }

    @Test
    void validatesBodiesIdsAndRanges() throws Exception {
        mockMvc.perform(post("/settlement-format")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"startDate":"2026-10-01","endDate":"2026-09-01","amount":0}
                                """))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/settlement-format/0")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/settlement-format/batch")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/settlement-format/list").param("amountMin", "20").param("amountMax", "10"))
                .andExpect(status().isBadRequest());
    }

    private String validBody() {
        return """
                {
                  "startDate":"2026-09-01",
                  "endDate":"2026-09-30",
                  "amount":150000,
                  "settlementStatus":"READY"
                }
                """;
    }

    private SettlementResponse response(Long id) {
        return new SettlementResponse(
                id,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                BigDecimal.valueOf(150000),
                SettlementStatus.READY,
                null,
                null,
                null);
    }
}
