package com.seminarhub.security.dto;

import javassist.Loader;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;


/**
 * [ 2023-07-21 daeho.kang ]
 * Description : JwtTokenPayload에 사용될 DTO Class입니다.
 * member_id가 일종의 고유값으로 사용될것이기에 member_id를 가져옵니다.
 * 권한처리를 위해서 member_role을 가져옵니다. SimpleGrantedAuthority 타입으로 "ROLE_"이 접두사로 붙여질것이라는것을 보여주게했습니다.
 * GrantedAuthority 와는 다른 쓰임새로써, SimpleGrantedAuthority를 사용했습니다.
 *
 * JWTTokenPaylaodDTO Example
    {
     "iat": 1689905187,
     "exp": 1692497187,
     "sub": "daeho.kang2@naver.com",
     "roles": "ROLE_ADMIN "
    }
 */
@Log4j2
@Getter
@Setter
@Builder
@ToString
public class JwtTokenPayloadDTO {
    private Long member_no;
    private String member_id;
    private Set<SimpleGrantedAuthority> member_role;

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : Set<SimpleGrantedAuthority>에 있는 값들을 String으로 반환합니다.
     */
    public String getMember_Role_ToString(){
        StringBuilder result = new StringBuilder();
        for (SimpleGrantedAuthority authority : member_role) {
            result.append(authority.getAuthority()).append(" ");
        }
        return result.toString();
    }

}
