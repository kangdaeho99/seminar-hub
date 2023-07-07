package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.Member;
import javassist.bytecode.DuplicateMemberException;

import java.util.List;


public interface MemberService {
    Long register(MemberDTO memberDTO) throws DuplicateMemberException;

    MemberDTO get(Long member_no);

    void modify(MemberDTO memberDTO);

    void remove(Long member_no);


    default Member dtoToEntity(MemberDTO memberDTO){
        Member member = Member.builder()
                .member_no(memberDTO.getMember_no())
                .member_id(memberDTO.getMember_id())
                .member_password(memberDTO.getMember_password())
                .member_nickname(memberDTO.getMember_nickname())
                .member_from_social(memberDTO.isMember_from_social())
                .build();
        return member;
    }

    default MemberDTO entityToDTO(Member member){
        MemberDTO memberDTO = MemberDTO.builder()
                .member_no(member.getMember_no())
                .member_id(member.getMember_id())
                .member_password(member.getMember_password())
                .member_nickname(member.getMember_nickname())
                .member_from_social(member.isMember_from_social())
                .build();

        return memberDTO;
    }

}
