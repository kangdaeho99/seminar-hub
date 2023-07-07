package com.seminarhub.controller;


import com.seminarhub.dto.MemberDTO;
import com.seminarhub.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "Member API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value ="")
    @Operation(summary = "Register a new member")
    public ResponseEntity<Long> register(@RequestBody MemberDTO memberDTO) throws DuplicateMemberException {
        log.info("-----------------register--------------");
        log.info(memberDTO);

        Long member_no = memberService.register(memberDTO);
        return new ResponseEntity<>(member_no, HttpStatus.OK);
    }

    @GetMapping(value ="/{member_no}")
    @Operation(summary = "Get member information by member_no")
    public ResponseEntity<MemberDTO> read(@PathVariable("member_no") long member_no){
        log.info("-------------------read----------------------");
        log.info(member_no);

        return new ResponseEntity<>(memberService.get(member_no), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{member_no}", produces= MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Remove a member by member_no")
    public ResponseEntity<String> remove(@PathVariable("member_no") Long member_no){
        log.info("-------------------remove---------------------");
        log.info(member_no);
        memberService.remove(member_no);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value ="/{member_no}", produces = MediaType.TEXT_PLAIN_VALUE)

    @Operation(summary = "Modify a member")
    public ResponseEntity<String> modify(@RequestBody MemberDTO memberDTO){
        log.info("-----------------modify----------------");
        log.info(memberDTO);

        memberService.modify(memberDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }

}
