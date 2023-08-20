package com.seminarhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.dto.MemberDTO;
import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import com.seminarhub.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore......");
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : '/api/v1/member' Register Test
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
        mockMvc.perform(post("/api/v1/member")
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
     * Description : '/api/v1/member/{member_no}' get Test
     */
    @Test
    @DisplayName("Get a Member Test")
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
        String member_id = "daeho.kang@hello.com";
        when(memberService.get(member_id)).thenReturn(memberDTO);

        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();

        // When
        mockMvc.perform(get("/api/v1/member/"+member_id)
                        .header("Authorization", "Bearer "+ jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO))
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
        verify(memberService).get(member_id);

    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : [DELETE] '/api/v1/member/{member_id}' remove Test
     */
    @Test
    @DisplayName("Remove a Member Test")
    void removeMemberTest() throws Exception {
        // given
        String member_id = "daeho.kang@hello.com";
        doNothing().when(memberService).remove(member_id);

        // when
        mockMvc.perform(delete("/api/v1/member/{member_id}", member_id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(memberService).remove(member_id);
    }


    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : [PUT] '/api/v1/member/' modify Test
     *
     */
    @Test
    @DisplayName("Modify a Member Test")
    void modifyMemberTest() throws Exception {
        // given
        MemberDTO memberDTO = MemberDTO.builder()
                .member_no(123L) // Use the actual member_no you want to test
                .member_id("member_id")
                .member_password("123123123")
                .member_nickname("new_nickname")
                .member_from_social(false)
                .build();

        // then
        mockMvc.perform(put("/api/v1/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(memberService).modify(memberDTO);
    }
}
