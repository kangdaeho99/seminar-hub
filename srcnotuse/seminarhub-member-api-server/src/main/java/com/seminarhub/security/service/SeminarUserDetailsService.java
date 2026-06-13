package com.seminarhub.security.service;

import com.seminarhub.entity.Member;
import com.seminarhub.repository.MemberRepository;
import com.seminarhub.security.dto.MemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * [ 2023-07-11 daeho.kang ]
 * Description: Custom implementation of the Spring Security UserDetailsService.
 * It is responsible for loading user-specific data for authentication and authorization.
 * private final : Creates a final constructor using @RequiredArgsConstructor for the memberRepository.
 */
@Log4j2
@Service //Bean
@RequiredArgsConstructor
public class SeminarUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    /**
     * [ 2023-07-11 daeho.kang ]
     * Description:
     * Executes findByEmail using the username, where username represents the member's email.
     * If the user does not exist, UsernameNotFoundException is thrown.
     * Converts the retrieved Member to AuthMemberDTO type (extending User type) to be processed as UserDetails.
     * Transforms authMemberRole into SimpleGrantedAuthority, which is used by Spring Security, adding the 'ROLE_' prefix.
     *
     * @param username the username identifying the user whose data is required.
     * @return UserDetails object containing user-specific authentication and authorization information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("SeminarUserDetailsService loadUserByUsername : "+username);

        Optional<Member> result = memberRepository.findByMember_id(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check Email");
        }

        Member member = result.get();

        log.info("------------------------");
        log.info(member);


        MemberAuthDTO memberAuthDTO = new MemberAuthDTO(
                member.getMember_no(),
                member.getMember_id(),
                member.getMember_password(),
                member.getMember_nickname(),
                member.isMember_from_social(),
                member.getMember_role_set()
                        .stream()
                        .map(memberRole -> new SimpleGrantedAuthority( "ROLE_"+ memberRole.getRole().getRole_type()))
                        .collect(Collectors.toSet())
        );


        return memberAuthDTO;
    }
}
