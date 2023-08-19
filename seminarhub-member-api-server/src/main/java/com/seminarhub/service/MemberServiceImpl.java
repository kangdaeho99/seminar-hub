package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.core.entity.Member;
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

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description :
     * member_id을 통해 이미 중복하는 아이디가 존재하는지 확인합니다. 중복일시 DuplicateSeminarException 이 발생합니다.
     * 통과하였다면, 세미나가 저장됩니다.
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
     * Description :
     * member_id를 통해 seminar의 정보를 가져옵니다.
     * 정보가 존재한다면, 해당 데이터를 DTO로 변환하여 전달합니다.
     */
    @Override
    public MemberDTO get(String member_id) {
        Optional<Member> result = memberRepository.findByMember_id(member_id);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description :
     * member_id 통해 해당 값이 존재하는지 확인하고, 존재한다면 수정합니다. (seminar_no가 아닌 member_id 통해 진행합니다.)
     * member_id 값이 Unique 입니다.
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
     * Description :
     * member_id을 통해 값이 존재하는지 확인하고, 값이 존재한다면 SOFT DELETE 처리합니다.
     */
    @Override
    public void remove(String member_id) {
        Optional<Member> result = memberRepository.findByMember_id(member_id);
        if(result.isPresent()){
            memberRepository.deleteByMember_id(member_id);
        }
    }


}
