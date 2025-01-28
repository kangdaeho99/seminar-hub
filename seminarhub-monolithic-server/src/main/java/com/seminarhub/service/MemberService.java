package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;

import java.util.List;

public interface MemberService {
    int createMember(MemberDTO member);
    MemberDTO getMemberById(int member_no);
    List<MemberDTO> getMembersByName(String name);
    int updateMember(MemberDTO member);
    int softDeleteMember(Long memberNo);
    boolean validateMember(String id, String pw);
    String generateToken(String id) throws Exception;
}