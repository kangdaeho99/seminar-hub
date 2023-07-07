package com.seminarhub.service;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.repository.MemberRepository;
import javassist.bytecode.DuplicateMemberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MemberServiceImpl.class})
public class MemberServiceTests {
    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberService Register Test
     */
    @DisplayName("Member Service Register Test")
    @Test
    public void registerMemberTest() throws DuplicateMemberException {
        // given
        Member member = Member.builder()
                .member_no((long)123)
                .member_id("member_id")
                .member_password("member_password")
                .member_nickname("member_nickname")
                .member_from_social(false)
                .build();

        Mockito.when(memberRepository.save(refEq(member))).thenReturn(member);

        // when
        MemberDTO memberDTO = MemberDTO.builder()
                .member_no((long)123)
                .member_id("member_id")
                .member_password("member_password")
                .member_nickname("member_nickname")
                .member_from_social(false)
                .build();
        Long result = memberService.register(memberDTO);

        // then
        Assertions.assertEquals(result, 123);
        verify(memberRepository).save(refEq(member));
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberService Get Test
     */
    @DisplayName("Member Service Get Test")
    @Test
    public void getMemberTest(){
        // given
        Member existingMember = Member.builder().member_no((long)123).member_id("member_id").member_password("member_password").member_nickname("member_nickname").member_from_social(false).build();
        Mockito.when(memberRepository.findByMember_no((long) 123)).thenReturn(Optional.of(existingMember));

        // when
        MemberDTO memberDTO = memberService.get((long)123);

        // then
        Assertions.assertEquals(memberDTO.getMember_no(), 123L);
        Assertions.assertEquals(memberDTO.getMember_id(), "member_id");
        Assertions.assertEquals(memberDTO.getMember_password(), "member_password");
        Assertions.assertEquals(memberDTO.getMember_nickname(), "member_nickname");
        Assertions.assertEquals(memberDTO.isMember_from_social(), false);

        verify(memberRepository).findByMember_no((long) 123);
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberService modify Test
     */
    @DisplayName("Member Service Modify Test")
    @Test
    public void modifyMemberTest() {
        // given
        Member existingMember = Member.builder().member_no((long)123).member_id("member_id").member_password("member_password").member_nickname("member_nickname").member_from_social(false).build();

        Mockito.when(memberRepository.findById(123L)).thenReturn(Optional.of(existingMember));

        MemberDTO memberDTO = MemberDTO.builder().member_no(123L).member_nickname("modified_member_nickname").build();
        // when
        memberService.modify(memberDTO);

        // then
        Mockito.verify(memberRepository).save(Mockito.any(Member.class));
        Assertions.assertEquals("modified_member_nickname", existingMember.getMember_nickname());
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberService SOFT Remove Test
     */

    @DisplayName("Member Service Soft Remove Test")
    @Test
    public void removeTest() {
        // given
        LocalDateTime del_dt = LocalDateTime.now();
        Member existingMember = Member.builder().member_no((long)123).member_id("member_id").member_password("member_password").member_nickname("member_nickname").member_from_social(false).del_dt(del_dt).build();
        Mockito.when(memberRepository.findById(123L)).thenReturn(Optional.of(existingMember));

        // when
        memberService.remove(123L);

        // then
        Mockito.verify(memberRepository).deleteByMember_no(123L);
        Assertions.assertNotNull(memberRepository.findById(123L).get().getDel_dt());
    }

}
