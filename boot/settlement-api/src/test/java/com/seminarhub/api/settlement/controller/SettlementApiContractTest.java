package com.seminarhub.api.settlement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seminarhub.api.settlement.exception.SettlementDateNotFoundException;
import com.seminarhub.global.error.SettlementExceptionHandler;
import com.seminarhub.api.settlement.facade.SettlementFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SettlementApiContractTest {

    private SettlementFacade settlementFacade;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        settlementFacade = mock(SettlementFacade.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new SettlementController(settlementFacade))
                .setControllerAdvice(new SettlementExceptionHandler())
                .build();
    }

    @Test
    void returnsTheCommonSuccessResponse() throws Exception {
        mockMvc.perform(post("/settlement/read-committed/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memberSeminarId":1,"targetDate":"2026-09-20"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isMap());
    }

    @Test
    void keepsNotFoundStatusForMissingSettlementDates() throws Exception {
        doThrow(new SettlementDateNotFoundException(1L))
                .when(settlementFacade)
                .updateWithReadCommitted(any());

        mockMvc.perform(post("/settlement/read-committed/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memberSeminarId":1,"targetDate":"2026-09-20"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.origin").value("SEMINAR_HUB"))
                .andExpect(jsonPath("$.code").value(1000));
    }

    @Test
    void mapsConcurrencyFailuresToConflict() throws Exception {
        doThrow(new PessimisticLockingFailureException("locked"))
                .when(settlementFacade)
                .updateWithReadCommitted(any());

        mockMvc.perform(post("/settlement/read-committed/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memberSeminarId":1,"targetDate":"2026-09-20"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.origin").value("COMMON"))
                .andExpect(jsonPath("$.code").value(409));
    }

    @Test
    void mapsUnexpectedFailuresToInternalServerError() throws Exception {
        doThrow(new RuntimeException("unexpected"))
                .when(settlementFacade)
                .updateWithReadCommitted(any());

        mockMvc.perform(post("/settlement/read-committed/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memberSeminarId":1,"targetDate":"2026-09-20"}
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.origin").value("COMMON"))
                .andExpect(jsonPath("$.code").value(500));
    }
}
