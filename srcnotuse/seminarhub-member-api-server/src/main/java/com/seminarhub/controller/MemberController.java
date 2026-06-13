package com.seminarhub.controller;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.RoleType;
import com.seminarhub.dto.MemberWithMember_SeminarDTO;
import com.seminarhub.security.annotation.CheckRole;
import com.seminarhub.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * [2023-08-30 daeho.kang]
 * Description: Controller responsible for managing member-related operations.
 * Handles URL mappings under ["/api/v1/member"].
 */
@RestController
@Log4j2
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "2. Member API")
public class MemberController {
    private final MemberService memberService;

    /**
     * [2023-08-23 daeho.kang]
     * Description: Register a new member (Accepts MemberDTO to register a new member.)
     * Utilizes the @RequestBody annotation to receive a JSON-formatted MEMBERDTO.
     */
    @PostMapping(value ="/")
    @Operation(summary = "1. Register a new member")
    public ResponseEntity<Long> register(@RequestBody MemberDTO memberDTO) throws DuplicateMemberException {
        log.info("-----------------register--------------");
        log.info(memberDTO);

        Long member_no = memberService.register(memberDTO);
        return new ResponseEntity<>(member_no, HttpStatus.OK);
    }

    /**
     * [2023-08-30 daeho.kang]
     * Description: Get member information by member_id (Retrieve user information using member_id.)
     */
//    @CheckRole(roles = {RoleType.USER})
    @GetMapping(value ="/{member_id}")
    @Operation(summary = "2. Get member information by member_no")
    public ResponseEntity<MemberDTO> read(@PathVariable("member_id") String member_id){
        log.info("-------------------read----------------------");
        log.info(member_id);

        return new ResponseEntity<>(memberService.get(member_id), HttpStatus.OK);
    }

    /**
     * [2023-08-30 daeho.kang]
     * Description: Remove a member by member_id (Remove or delete a member using member_id. Utilizes JPA's Dirty Checking & SOFT_DELETE.)
     */
    @DeleteMapping(value = "/{member_id}", produces= MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "3. Remove a member by member_no")
    public ResponseEntity<String> remove(@PathVariable("member_id") String member_id){
        log.info("-------------------remove---------------------");
        log.info(member_id);
        memberService.remove(member_id);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }


    /**
     * [2023-08-30 daeho.kang]
     * Description: Modify member information (Modify user information using memberDTO. Utilizes JPA's Dirty Checking.)
     */
    @PutMapping(value ="/", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "4. Modify a member")
    public ResponseEntity<String> modify(@RequestBody MemberDTO memberDTO){
        log.info("-----------------modify----------------");
        log.info(memberDTO);

        memberService.modify(memberDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }

    /**
     * [2023-08-30 daeho.kang]
     * Description: Get member information with associated Member_Seminar details (Retrieve user information along with Member_Seminar details when calling member information. Utilizes openFeign to call from seminarhub-member-seminar-api-server.)
     */
    @CheckRole(roles = {RoleType.USER})
    @GetMapping(value ="/with-member-seminar/{member_id}")
    @Operation(summary = "5. Get member information With Seminar by member_id")
    public ResponseEntity<MemberWithMember_SeminarDTO> findAllMembersWithSeminar(@PathVariable("member_id") String member_id){
        log.info("-------------------findAllMembersWithSeminar----------------------");
        log.info(member_id);

        return new ResponseEntity<>(memberService.getMemberWithMember_Seminar(member_id), HttpStatus.OK);
    }

}
