package com.seminarhub.repository;

import com.seminarhub.dto.MemberDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    public MemberRepository memberRepository;

    // CREATE 테스트
    @Test
    public void testInsertMember() {
        MemberDTO member = new MemberDTO();
        member.setId("test_user");
        member.setName("Test Name");
        member.setPw("password123");
        member.setGender("M");
        member.setAge(30L);
        member.setAddress("Seoul, Korea");
        member.setJob("Developer");
        member.setGrade("bronze");
        member.setPoints(1000L);

        int result = memberRepository.Insert(member);
        Assertions.assertEquals(1, result, "Member should be saved successfully.");
    }

    @Test
    public void testFindMember(){
        MemberDTO memberDTO = memberRepository.findMemberById(100);
        System.out.println(memberDTO.toString());
    }

    // SELECT LIST 테스트
    @Test
    public void testFindMembersByName() {
        String name = "채호현"; // 테스트에 맞는 이름으로 변경
        List<MemberDTO> members = memberRepository.findMemberByName(name);
        Assertions.assertFalse(members.isEmpty(), "Members list should not be empty.");
        members.forEach(System.out::println);
    }

    // UPDATE 테스트
    @Test
    public void testUpdateMember() {
        MemberDTO member = new MemberDTO();
        member.setMember_no(1L); // 테스트에 맞는 실제 member_no 값으로 변경
        member.setId("updated_user");
        member.setName("Updated Name");
        member.setPw("newpassword123");
        member.setGender("F");
        member.setAge(28L);
        member.setAddress("Busan, Korea");
        member.setJob("Designer");
        member.setGrade("silver");
        member.setPoints(2000L);

        int result = memberRepository.update(member);
        Assertions.assertEquals(1, result, "Member should be updated successfully.");
    }

    // DELETE (Soft Delete) 테스트
    @Test
    public void testSoftDeleteMember() {
        Long memberNo = 1L; // 테스트에 맞는 실제 member_no 값으로 변경
        int result = memberRepository.softDelete(memberNo);
        Assertions.assertEquals(1, result, "Soft delete should be successful.");
    }
}
