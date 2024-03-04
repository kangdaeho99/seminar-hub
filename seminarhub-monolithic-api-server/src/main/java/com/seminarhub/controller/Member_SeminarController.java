package com.seminarhub.controller;


import com.seminarhub.dto.MemberSeminarRegisterRequestDTO;
import com.seminarhub.service.MemberService;
import com.seminarhub.service.Member_SeminarService;
import com.seminarhub.service.SeminarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/v1/member_seminar")
@RequiredArgsConstructor
@Tag(name = "4. Member_Seminar API")
public class Member_SeminarController {
    private final MemberService memberService;
    private final Member_SeminarService member_seminarService;
    private final SeminarService seminarService;

    @PostMapping(value = "")
    @Operation(summary = "1. Register for Seminar")
    public ResponseEntity<Long> registerForSeminar(@RequestBody MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO) throws Exception {
        Long member_seminarno = member_seminarService.registerForSeminar(memberSeminarRegisterRequestDTO);
        return new ResponseEntity<>(member_seminarno, HttpStatus.OK);
    }

}
