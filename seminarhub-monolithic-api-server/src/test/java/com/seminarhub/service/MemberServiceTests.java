package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class MemberServiceTests {

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @DisplayName("Member Service FindMemberByMember_id Test")
    @Test
    public void getMemberTest(){
        // given
        Member existingMember = Member.builder()
                .member_no((long) 2994)
                .member_id("passionfruit200@naver.com")
                .member_password("member_password")
                .member_nickname("member_nickname")
                .member_from_social(false)
                .build();
        Mockito.when(memberRepository.findByMember_id("passionfruit200@naver.com")).thenReturn(Optional.of(existingMember));

        // when
        MemberDTO memberDTO = memberService.get("passionfruit200@naver.com");

        // then
        Assertions.assertEquals(memberDTO.getMember_no(), 2994L);
        Assertions.assertEquals(memberDTO.getMember_id(), "passionfruit200@naver.com");
        Assertions.assertEquals(memberDTO.getMember_password(), "member_password");
        Assertions.assertEquals(memberDTO.getMember_nickname(), "member_nickname");
        Assertions.assertEquals(memberDTO.isMember_from_social(), false);

        verify(memberRepository).findByMember_id("passionfruit200@naver.com");
    }

}
