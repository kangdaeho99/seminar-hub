package com.seminarhub.controller;


import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.RoleType;
import com.seminarhub.dto.MemberSeminarDTO;
import com.seminarhub.security.annotation.CheckRole;
import com.seminarhub.service.MemberSeminarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description: SeminarController
 */
@RestController
@Log4j2
@RequestMapping("/api/v1/member-seminar")
@RequiredArgsConstructor
@Tag(name = "3. Seminar API")
public class MemberSeminarController {
    private final MemberSeminarService memberSeminarService;

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Register a new seminar
     */
    @PostMapping(value ="")
    @Operation(summary = "1. Register a new seminar")
    public ResponseEntity<Long> register(@RequestBody MemberSeminarDTO memberSeminarDTO) throws DuplicateSeminarException {
        log.info("-----------------register--------------");
        log.info(memberSeminarDTO);

        Long member_seminar_no = memberSeminarService.register(memberSeminarDTO);
        return new ResponseEntity<>(member_seminar_no, HttpStatus.OK);
    }

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve seminar information
     * Accessible only to users with USER role, as indicated by @CheckRole annotation.
     */
//    @CheckRole(roles = {RoleType.USER})
    @GetMapping(value ="/{member_seminar_no}")
    @Operation(summary = "2. Get member_seminar information by member_seminar_no")
    public ResponseEntity<MemberSeminarDTO> read(@PathVariable("member_seminar_no") Long member_seminar_no){
        log.info("-------------------read----------------------");
        log.info(member_seminar_no);

        return new ResponseEntity<>(memberSeminarService.get(member_seminar_no), HttpStatus.OK);
    }

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Remove a seminar (SOFT deletion)
     */
    @DeleteMapping(value = "/{member_seminar_no}", produces= MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "3. Remove a member_seminar by member_seminar_no")
    public ResponseEntity<String> remove(@PathVariable("member_seminar_no") Long member_seminar_no){
        log.info("-------------------remove---------------------");
        log.info(member_seminar_no);
        memberSeminarService.remove(member_seminar_no);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Find all Member_Seminar information by member_id
     * Accessible only to users with USER role, as indicated by @CheckRole annotation.
     */
    @CheckRole(roles = {RoleType.USER})
    @GetMapping(value ="/findAllByMember_id/{member_id}")
    @Operation(summary = "4. findAll Member_Seminar information by member_id")
    public ResponseEntity<List<MemberSeminarDTO>> findAllMember_SeminarByMember_id(@PathVariable("member_id") String member_id){
        log.info("-------------------read----------------------");
        log.info(member_id);

        return new ResponseEntity<>(memberSeminarService.getListByMember_id(member_id), HttpStatus.OK);
    }

}
