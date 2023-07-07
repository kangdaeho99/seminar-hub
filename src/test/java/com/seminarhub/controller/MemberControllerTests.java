package com.seminarhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.dto.MemberDTO;
import com.seminarhub.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // To put Data in Body As a JSON Object
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : '/member' Register Test
     */
    @Test
    @DisplayName("Register a new member")
    void registerMemberTest() throws Exception {
        // Given
        MemberDTO memberDTO = MemberDTO.builder()
                .member_no(123L)
                .member_id("member_id")
                .member_password("123123123")
                .member_nickname("member_nickname")
                .member_from_social(false)
                .build();
        when(memberService.register(memberDTO)).thenReturn(123L);

        // When
        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(123L))
                .andDo(print());

        // Then
        verify(memberService).register(memberDTO);
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : '/member/{member_no}' get Test
     */
    @Test
    @DisplayName("Register a new member")
    void getMemberTest() throws Exception {
        // Given
        MemberDTO memberDTO = MemberDTO.builder()
                .member_no(123L)
                .member_id("member_id")
                .member_password("123123123")
                .member_nickname("member_nickname")
                .member_from_social(false)
                .build();
        Long member_no = 123L;
        when(memberService.get(member_no)).thenReturn(memberDTO);

        // When
        mockMvc.perform(get("/member/"+member_no)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member_no").value(123L))
                .andExpect(jsonPath("$.member_id").value("member_id"))
                .andExpect(jsonPath("$.member_password").value("123123123"))
                .andExpect(jsonPath("$.member_nickname").value("member_nickname"))
                .andExpect(jsonPath("$.member_from_social").value(false))
                .andDo(print());

        // Then
        verify(memberService).get(member_no);

    }
}
