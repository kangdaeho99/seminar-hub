package com.seminarhub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seminarhub.dto.CursorResponse;
import com.seminarhub.dto.Pagination;
import com.seminarhub.dto.SettlementItemResponse;
import com.seminarhub.dto.SettlementResponse;
import com.seminarhub.enums.SettlementStatus;
import com.seminarhub.exception.SettlementExceptionHandler;
import com.seminarhub.facade.SettlementFacade;
import com.seminarhub.facade.SettlementItemFacade;
import com.seminarhub.global.resolver.CursorRequestArgumentResolver;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SettlementReadApiContractTest {

    private SettlementFacade settlementFacade;
    private SettlementItemFacade itemFacade;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        settlementFacade = mock(SettlementFacade.class);
        itemFacade = mock(SettlementItemFacade.class);
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setOneIndexedParameters(true);
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new SettlementController(settlementFacade), new SettlementItemController(itemFacade))
                .setCustomArgumentResolvers(new CursorRequestArgumentResolver(), pageableResolver)
                .setControllerAdvice(new SettlementExceptionHandler())
                .build();
    }

    @Test
    void returnsSettlementListPageAndCursorContracts() throws Exception {
        SettlementResponse response = new SettlementResponse(
                3L, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), BigDecimal.TEN,
                SettlementStatus.COMPLETED, null, null, null);
        when(settlementFacade.findAll(any())).thenReturn(List.of(response));
        when(settlementFacade.findPage(any(), any())).thenReturn(Pagination.of(
                new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1)));
        when(settlementFacade.findByCursor(any(), any()))
                .thenReturn(CursorResponse.of(List.of(response), 10, SettlementResponse::id));

        mockMvc.perform(get("/settlement/list").param("ids", "3,4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(3));

        mockMvc.perform(get("/settlement/page").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pagination.currentPage").value(1))
                .andExpect(jsonPath("$.data.pagination.endIdx").value(1));

        mockMvc.perform(get("/settlement/cursor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cursor.hasNext").value(false))
                .andExpect(jsonPath("$.data.items[0].settlementStatus").value("COMPLETED"));
    }

    @Test
    void returnsSettlementItemContract() throws Exception {
        SettlementItemResponse response = new SettlementItemResponse(
                9L, 3L, 12L, BigDecimal.TEN, null, null, null);
        when(itemFacade.findAll(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/settlement/items/list").param("settlementIds", "3,4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].settlementId").value(3))
                .andExpect(jsonPath("$.data[0].memberSeminarId").value(12));
    }

    @Test
    void rejectsInvalidSearchRange() throws Exception {
        mockMvc.perform(get("/settlement/page")
                        .param("startDateFrom", "2026-02-01")
                        .param("startDateTo", "2026-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }
}
