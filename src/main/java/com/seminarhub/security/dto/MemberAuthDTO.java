package com.seminarhub.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class MemberAuthDTO extends User {

    private Long member_no;
    private String member_id;
    private String member_password;
    private String member_nickname;
    private boolean member_from_social;

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description : It Calls User Constructor
     */
    public MemberAuthDTO(Long member_no, String member_id, String member_password, String member_nickname, boolean member_from_social, Collection<? extends GrantedAuthority> authorities){
        super(member_id, member_password, authorities);
        this.member_no = member_no;
        this.member_nickname = member_nickname;
        this.member_from_social = member_from_social;
    }
}
