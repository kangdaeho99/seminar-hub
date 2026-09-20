package com.seminarhub.global.resolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seminarhub.exception.SettlementExceptionHandler;
import com.seminarhub.global.annotation.CursorDefault;
import com.seminarhub.global.dto.CursorRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class CursorRequestArgumentResolverTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CursorController())
                .setCustomArgumentResolvers(new CursorRequestArgumentResolver())
                .setControllerAdvice(new SettlementExceptionHandler())
                .build();
    }

    @Test
    void resolvesDefaultsAndDirection() throws Exception {
        mockMvc.perform(get("/cursor-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.direction").value("DESC"));

        mockMvc.perform(get("/cursor-test").param("size", "900").param("cursor", "7").param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(500))
                .andExpect(jsonPath("$.cursor").value(7))
                .andExpect(jsonPath("$.direction").value("ASC"));
    }

    @Test
    void rejectsInvalidValues() throws Exception {
        mockMvc.perform(get("/cursor-test").param("size", "0")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/cursor-test").param("cursor", "x")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/cursor-test").param("direction", "sideways")).andExpect(status().isBadRequest());
    }

    @RestController
    static class CursorController {
        @GetMapping("/cursor-test")
        CursorRequest resolve(@CursorDefault CursorRequest request) {
            return request;
        }
    }
}
