package com.seminarhub.controller;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.annotation.CurrentUser;
import com.seminarhub.annotation.UseGuard;
import com.seminarhub.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Create member
    @UseGuard()
    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody MemberDTO member) {
        int result = memberService.createMember(member);
        if (result > 0)
            return ResponseEntity.ok("Member created successfully");

        return ResponseEntity.status(500).body("Failed to create member");
    }

    // Get member by ID
    @UseGuard()
    @GetMapping("/{member_no}")
    public ResponseEntity<MemberDTO> getMemberById(@CurrentUser() MemberDTO user, @PathVariable int member_no) {
        System.out.println("CurrentUser MemberDTO Information:"+user.getId());
        MemberDTO member = memberService.getMemberById(member_no);
        if (member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Get members by name
    @GetMapping("/search")
    public ResponseEntity<List<MemberDTO>> getMembersByName(@RequestParam String name) {
        List<MemberDTO> members = memberService.getMembersByName(name);
        if (!members.isEmpty()) {
            return ResponseEntity.ok(members);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Update member
    @PutMapping("/{member_no}")
    public ResponseEntity<String> updateMember(@PathVariable Long member_no, @RequestBody MemberDTO member) {
        member.setMember_no(member_no);  // Ensure the member_no is set in the DTO
        int result = memberService.updateMember(member);
        if (result > 0) {
            return ResponseEntity.ok("Member updated successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to update member");
        }
    }

    // Soft delete member
    @DeleteMapping("/{member_no}")
    public ResponseEntity<String> softDeleteMember(@PathVariable Long member_no) {
        int result = memberService.softDeleteMember(member_no);
        if (result > 0) {
            return ResponseEntity.ok("Member deleted successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to delete member");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO member) throws Exception {
        boolean isValidUser = memberService.validateMember(member.getId(), member.getPw());
        if(isValidUser){
            String token = memberService.generateToken(member.getId());
            return ResponseEntity.ok(token);
        }
        throw new AuthenticationException("Not Authorized");
    }
}