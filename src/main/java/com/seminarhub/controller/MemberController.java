package com.seminarhub.controller;


import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.RoleType;
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

@RestController
@Log4j2
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "2. Member API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value ="")
    @Operation(summary = "1. Register a new member")
    public ResponseEntity<Long> register(@RequestBody MemberDTO memberDTO) throws DuplicateMemberException {
        log.info("-----------------register--------------");
        log.info(memberDTO);

        Long member_no = memberService.register(memberDTO);
        return new ResponseEntity<>(member_no, HttpStatus.OK);
    }


    @CheckRole(roles = {RoleType.USER})
    @GetMapping(value ="/{member_id}")
    @Operation(summary = "2. Get member information by member_no")
    public ResponseEntity<MemberDTO> read(@PathVariable("member_id") String member_id){
        log.info("-------------------read----------------------");
        log.info(member_id);

        return new ResponseEntity<>(memberService.get(member_id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{member_id}", produces= MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "3. Remove a member by member_no")
    public ResponseEntity<String> remove(@PathVariable("member_id") String member_id){
        log.info("-------------------remove---------------------");
        log.info(member_id);
        memberService.remove(member_id);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value ="", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "4. Modify a member")
    public ResponseEntity<String> modify(@RequestBody MemberDTO memberDTO){
        log.info("-----------------modify----------------");
        log.info(memberDTO);

        memberService.modify(memberDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }

}
