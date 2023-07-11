package com.seminarhub.repository;

import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Role;
import com.seminarhub.entity.Role;
import com.seminarhub.entity.RoleType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;

/**
 * [ 2023-06-27 daeho.kang ]
 * Description : No Test For JpaRepository Method
 * Only for QueryDSL or JPQL Custom Method
 */
@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;
    private final String member_id= "daeho.kang2@naver.com";
    private final String member_password= "123123123";
    private final String member_nickname = "hello";
    private final boolean member_from_social = false;


    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : Setting the Data for Test
     */
    @BeforeEach
    public void setup() throws Exception{
        if(memberRepository.countByMember_id(member_id) == 0){
            // Create member
            Member member = Member.builder()
                    .member_id(member_id)
                    .member_password(passwordEncoder.encode(member_password))
                    .member_nickname(member_nickname)
                    .member_from_social(member_from_social)
                    .build();

//            Role userRole = Role.builder()
//                    .role_type(RoleType.USER)
//                    .build();
//            roleRepository.save(userRole);
            Role adminRole = roleRepository.findByRole_type(RoleType.ADMIN)
                    .orElseGet(() -> roleRepository.save(Role.builder().role_type(RoleType.ADMIN).build()));
            Role userRole = roleRepository.findByRole_type(RoleType.USER)
                    .orElseGet(() -> roleRepository.save(Role.builder().role_type(RoleType.USER).build()));

            // Create member_role
            Member_Role memberRole = Member_Role.builder()
                    .member(member)
                    .role(adminRole)
                    .build();

            // ADD Set member_role in member
            member.addMemberRole(memberRole);

            //Save Member
            memberRepository.save(member);

        }

    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberRepository getWithMember_id Test
     * Find Member by member_id
     */
    @DisplayName("findByMember_id Test")
    @Test
//    @Transactional
//    @Rollback(false)
    public void testGetWithMember_id(){
        // given, when
        Optional<Member> member = memberRepository.findByMember_id(member_id);

        // then
        assertNotNull(member.get());
        System.out.println(member.get());
    }



}
