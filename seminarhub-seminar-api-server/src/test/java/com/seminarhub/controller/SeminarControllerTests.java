package com.seminarhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;

import com.seminarhub.service.SeminarService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SeminarControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // To put Data in Body As a JSON Object
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    SeminarService seminarService;

    @Autowired
    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore......");
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : [POST] '/api/v1/seminar' Register Test
     */
    @Test
    @DisplayName("Register a new seminar")
    void registerMemberTest() throws Exception {
        // Given
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no(123L)
                .seminar_name("seminar_name")
                .seminar_explanation("123123123")
                .build();
        when(seminarService.register(seminarDTO)).thenReturn(123L);

        // When
        mockMvc.perform(post("/api/v1/seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seminarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(123L))
                .andDo(print());

        // Then
        verify(seminarService).register(seminarDTO);
    }

    /**
     * [ 2023-07-04 daeho.kang ]
     * Description : [GET] '/api/v1/seminar/{seminar_no}' get Test
     */
    @Test
    @DisplayName("Get a Seminar Test")
    void getSeminarTest() throws Exception {
        // Given
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no(123L)
                .seminar_name("seminar_name")
                .seminar_explanation("123123123")
                .build();
        Long seminar_no = 123L;
        String seminar_name = "daeho.kang@hello.com";
        when(seminarService.get(seminar_name)).thenReturn(seminarDTO);

        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();

        // When
        mockMvc.perform(get("/api/v1/seminar/"+seminar_name)
                        .header("Authorization", "Bearer "+ jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seminarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seminar_no").value(123L))
                .andExpect(jsonPath("$.seminar_name").value("seminar_name"))
                .andExpect(jsonPath("$.seminar_explanation").value("123123123"))
                .andDo(print());

        // Then
        verify(seminarService).get(seminar_name);

    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : [DELETE] '/api/v1/seminar/{seminar_name}' remove Test
     */
    @Test
    @DisplayName("Remove a Seminar Test")
    void removeMemberTest() throws Exception {
        // given
        String seminar_name = "seminar_name";
        doNothing().when(seminarService).remove(seminar_name);

        // when
        mockMvc.perform(delete("/api/v1/seminar/{seminar_name}", seminar_name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(seminarService).remove(seminar_name);
    }


    /**
     * [ 2023-08-10 daeho.kang ]
     * Description : [PUT] '/api/v1/seminar/' modify Test
     *
     */
    @Test
    @DisplayName("Modify a Seminar Test")
    void modifySeminarTest() throws Exception {
        // given
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no(123L) // Use the actual seminar_no you want to test
                .seminar_name("seminar_name")
                .seminar_explanation("modified_seminar_explanation")
                .build();

        // then
        mockMvc.perform(put("/api/v1/seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seminarDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(seminarService).modify(seminarDTO);
    }

}