package com.seminarhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.dto.MemberSeminarRegisterRequestDTO;
import com.seminarhub.service.Member_SeminarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Member_SeminarService memberSeminarService;

    @Test
    @DisplayName("MemberController registerForSeminar Tests")
    void registerForSeminarTest() throws Exception{
        // given
        MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO = MemberSeminarRegisterRequestDTO.builder()
                .member_id("passionfruit200@naver.com")
                .seminar_name("SeminarTest")
                .build();
        Mockito.when(memberSeminarService.registerForSeminar(memberSeminarRegisterRequestDTO)).thenReturn(3412L);

        // when
        mockMvc.perform(post("/api/v1/member_seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberSeminarRegisterRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3412L))
                .andDo(print());

        // then
        verify(memberSeminarService).registerForSeminar(memberSeminarRegisterRequestDTO);
    }



}

