package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.repository.MemberRepository;
import jakarta.transaction.Transactional;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * [ 2023-08-10 daeho.kang ]
 * Description: Implementation class for the MemberService interface.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements  MemberService{

    private final MemberRepository memberRepository;

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Registers a new member by checking for duplicate member_id.
     * If member_id is unique, saves the member information.
     *
     */
    @Override
    public Long register(MemberDTO memberDTO) throws DuplicateMemberException {
        Member member = dtoToEntity(memberDTO);
        log.info("=================Member Register======================");
        log.info(member);

        if(memberRepository.findByMember_id(memberDTO.getMember_id()).isPresent()){
            throw new DuplicateMemberException(memberDTO.getMember_id() + "is Already Exists");
        }
        memberRepository.save(member);
        return member.getMember_no();
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Retrieves member information based on the provided member_id.
     *
     */
    @Override
    public MemberDTO get(String member_id) {
        Optional<Member> result = memberRepository.findByMember_id(member_id);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    @Override
    public MemberDTO getMember_no(String member_id) {
        Optional<Member> result = memberRepository.findByMember_id(member_id);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Modifies member information based on the provided memberDTO.
     */
    @Override
    public void modify(MemberDTO memberDTO) {
        String member_id = memberDTO.getMember_id();
        Optional<Member> result = memberRepository.findByMember_id(member_id);
        if(result.isPresent()){
            Member member = result.get();
            member.setMember_nickname(memberDTO.getMember_nickname());
            memberRepository.save(member);
        }
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Removes a member by marking them as deleted using SOFT DELETE approach.
     * Using Jpd Dirty Checking
     */
    @Transactional
    @Override
    public void remove(String member_id) {
        Optional<Member> result = memberRepository.findByMember_id(member_id);
        if(result.isPresent()){
            result.get().setDel_dt(LocalDateTime.now()); //현재 시간으로 Dirty Checking
        }
    }


}
