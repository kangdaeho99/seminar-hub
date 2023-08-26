package com.seminarhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.dto.MemberSeminarDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.Seminar;
import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import com.seminarhub.service.MemberSeminarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberSeminarControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // To put Data in Body As a JSON Object
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MemberSeminarService memberSeminarService;

    private Long member_seminar_no = 1L;
    private String member_id = "daeho.kang@naver.com";
    private String seminar_name = "SeminarTest";
    private Member member = Member.builder()
            .member_id(member_id)
            .build();
    private Seminar seminar = Seminar.builder()
            .seminar_name(seminar_name)
            .build();

    @Autowired
    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore......");
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : [POST] '/api/v1/member-seminar' Register Test
     */
    @Test
    @DisplayName("Register a new member-seminar")
    void registerMember_SeminarTest() throws Exception {
        // Given
        MemberSeminarDTO memberSeminarDTO = MemberSeminarDTO.builder()
                .member_seminar_no(member_seminar_no)
                .member_id(member_id)
                .seminar_name(seminar_name)
                .build();

        when(memberSeminarService.register(memberSeminarDTO)).thenReturn(member_seminar_no);

        // When
        mockMvc.perform(post("/api/v1/member-seminar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSeminarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(member_seminar_no))
                .andDo(print());

        // Then
        verify(memberSeminarService).register(memberSeminarDTO);
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : [GET] '/api/v1/member-seminar/{seminar_no}' get Test
     */
    @Test
    @DisplayName("Get a Member_Seminar Test")
    void getMember_SeminarTest() throws Exception {
        // Given
        MemberSeminarDTO memberSeminarDTO = MemberSeminarDTO.builder()
                .member_seminar_no(member_seminar_no)
                .member_id(member_id)
                .seminar_name(seminar_name)
                .build();

        when(memberSeminarService.get(member_seminar_no)).thenReturn(memberSeminarDTO);

        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();

        // When
        mockMvc.perform(get("/api/v1/member-seminar/" + member_seminar_no)
                        .header("Authorization", "Bearer " + jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member_seminar_no").value(member_seminar_no))
                .andExpect(jsonPath("$.member_id").value(member_id))
                .andExpect(jsonPath("$.seminar_name").value(seminar_name))
                .andDo(print());

        // Then
        verify(memberSeminarService).get(member_seminar_no);

    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : [DELETE] '/api/v1/member-seminar/{seminar_name}' remove Test
     */
    @Test
    @DisplayName("Remove a Member_Seminar Test")
    void removeMember_SeminarTest() throws Exception {
        // given;
        doNothing().when(memberSeminarService).remove(member_seminar_no);

        // when
        mockMvc.perform(delete("/api/v1/member-seminar/{member_seminar_no}", member_seminar_no)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(memberSeminarService).remove(member_seminar_no);
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : [GET] '/api/v1/member-seminar/findAllByMember_id/{member_id}' get Test
     */
    @Test
    @DisplayName("find All Member_Seminar By Member_id Test")
    void findAllMember_SeminarByMember_idTest() throws Exception {
        // Given
        MemberSeminarDTO memberSeminarDTO = MemberSeminarDTO.builder()
                .member_seminar_no(member_seminar_no)
                .member_id(member_id)
                .seminar_name(seminar_name)
                .build();
        List<MemberSeminarDTO> memberSeminarDTOList = new ArrayList<MemberSeminarDTO>();
        memberSeminarDTOList.add(memberSeminarDTO);

        when(memberSeminarService.getListByMember_id(member_id)).thenReturn(memberSeminarDTOList);

        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();

        // When
        mockMvc.perform(get("/api/v1/member-seminar/findAllByMember_id/" + member_id)
                        .header("Authorization", "Bearer " + jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].member_seminar_no").value(member_seminar_no))
                .andExpect(jsonPath("$[0].member_id").value(member_id))
                .andExpect(jsonPath("$[0].seminar_name").value(seminar_name))
                .andDo(print());

        // Then
        verify(memberSeminarService).getListByMember_id(member_id);

    }

}