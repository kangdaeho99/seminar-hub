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

@Log4j2
@Service //Bean
@RequiredArgsConstructor
public class SeminarUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

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
//        log.info(member.getMember_role_set().stream().map(memberRole -> memberRole.getRole().getRole_type() ));
//        Member(member_no=5,
//                member_id=daeho.kang@naver.com,
//                member_password=123123123,
//                member_nickname=hello,
//                member_from_social=false,
//                del_dt=null,
//                member_role_set=
//                     [Member_Role
//                        (member_role_no=5,
//                         role=Role
//                                 (role_no=5, role_type=ADMIN))])


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
