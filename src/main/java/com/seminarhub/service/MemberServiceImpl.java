package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.repository.MemberRepository;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements  MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Long register(MemberDTO memberDTO) throws DuplicateMemberException {
        Member member = dtoToEntity(memberDTO);
        log.info("=================Member Register======================");
        log.info(member);

        if(memberRepository.countByMember_id(memberDTO.getMember_id()) != 0){
            throw new DuplicateMemberException(memberDTO.getMember_id() + "is Already Exists");
        }
        memberRepository.save(member);
        return member.getMember_no();
    }

    @Override
    public MemberDTO get(Long member_no) {
        Optional<Member> result = memberRepository.findByMember_no(member_no);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    @Override
    public void modify(MemberDTO memberDTO) {
        Long member_no = memberDTO.getMember_no();
        Optional<Member> result = memberRepository.findById(member_no);
        if(result.isPresent()){
            Member member = result.get();
            member.setMember_nickname(memberDTO.getMember_nickname());
            memberRepository.save(member);
        }
    }

    @Override
    public void remove(Long member_no) {
        Optional<Member> result = memberRepository.findById(member_no);
        if(result.isPresent()){
            System.out.println("result"+result);
            memberRepository.deleteByMember_no(member_no);
        }
    }


}
