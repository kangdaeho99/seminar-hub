package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.repository.MemberRepository;
import com.seminarhub.util.JWTUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements  MemberService{
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil = new JWTUtil();

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public int createMember(MemberDTO member) {
        return memberRepository.Insert(member);
    }

    @Override
    public MemberDTO getMemberById(int member_no) {
        return memberRepository.findMemberById(member_no);
    }

    @Override
    public List<MemberDTO> getMembersByName(String name) {
        return memberRepository.findMemberByName(name);
    }

    @Override
    public int updateMember(MemberDTO member) {
        return memberRepository.update(member);
    }

    @Override
    public int softDeleteMember(Long memberNo) {
        return memberRepository.softDelete(memberNo);
    }

    @Override
    public boolean validateMember(String id, String pw) {
        String realPw = memberRepository.findPwById(id).getPw();
        return pw.equals(realPw) ? true : false;
    }

    @Override
    public String generateToken(String id) throws Exception {
        return jwtUtil.generateToken(id);
    }

}
