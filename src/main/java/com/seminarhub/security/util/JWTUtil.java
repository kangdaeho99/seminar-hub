package com.seminarhub.security.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.dto.MemberAuthDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * JWTUtil은 스프링 환경이 아닌곳에서도 사용할 수 있도록 유틸리티 클래스로 설계합니다.
 */
@Log4j2
public class JWTUtil {
    private String secretKey = "secret";

    //1month
    private long expire = 60 * 24 * 30;


    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : generateToken()은 JWT토큰을 생성하는 역할을 합니다.
     * JWT 문자열 자체를 알면 누구든 API를 사용할 수 있다는 문제가 생기므로 만료기간(expire)값을 설정하고 'secret' 이라는 key를 이용해서 Signature 를 생성합니다.
     * expire 값을 : 30일로 설정했습니다.
     * 'sub'이라는 이름을 가진 Claim에는 사용자의 이메일 주소를 입력해주어서 나중에 사용할 수 있도록 구성합니다.
     *
     * Jwt Token Example
     {
         "iat": 1689905187,
         "exp": 1692497187,
         "sub": "daeho.kang2@naver.com",
         "roles": "ROLE_ADMIN "
     }
     */
    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
//                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : JwtTokenPayloadDTO와 함께 JWT토큰을 생성하는 역할을 합니다.
     * JWT 문자열 자체를 알면 누구든 API를 사용할 수 있다는 문제가 생기므로 만료기간(expire)값을 설정하고 'secret' 이라는 key를 이용해서 Signature 를 생성합니다.
     * expire 값을 : 30일로 설정했습니다.
     * 'sub'이라는 이름을 가진 Claim에는 사용자의 이메일 주소를 입력해주어서 나중에 사용할 수 있도록 구성합니다.
     * 'roles'이라는 이름을 가진 Claim에는 사용자의 roles를 입력하여, 권한 관련 처리를합니다. 입력예시로는 "ROLE_USER ROLE_ADMIN"입니다.
     * 'roles' 권한은 RoleType enum 값들에 값의 종류가 있습니다.
     *
     * Jwt Token Example
     {
         "iat": 1689905187,
         "exp": 1692497187,
         "sub": "daeho.kang2@naver.com",
         "roles": "ROLE_ADMIN ROLES_USER"
     }
     */
    public String generateTokenWithJwtTokenPayloadDTO(JwtTokenPayloadDTO jwtTokenPayloadDTO) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", jwtTokenPayloadDTO.getMember_id())
                .claim("roles", jwtTokenPayloadDTO.getMember_Role_ToString())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description : validateAndExtract() 는 JWT 인코딩된 문자열에서 원하는 값을 추출하는 역할을 합니다.
     * 예를들어 JWT가 이미 만료기간이 지난 것이라면 이 과정에서 expriation 이 발생하게 됩니다.
     * 또한 'sub' 이름으로 보관된 이메일도 반환하게 됩니다.
     * JWTUtil은 올바르게 생성되고, 검증이 가능한지 테스트를 진행하는 것이 좋습니다.
     * test 폴더 내에 security 패키지에 JWTTests 클래스를 작성하여 테스트 했습니다.
     *
     * Jwt Token Example
     {
         "iat": 1689905187,
         "exp": 1692497187,
         "sub": "daeho.kang2@naver.com",
     }
     */
    public String validateAndExtract(String tokenStr) throws Exception{
        String contentValue = null;

        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            log.info("------------------------");
            contentValue = claims.getSubject();
        } catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : JWT 인코딩된 문자열에서 원하는 값을 추출하는 역할을 합니다.
     * 예를들어 JWT가 이미 만료기간이 지난 것이라면 이 과정에서 expriation 이 발생하게 됩니다.
     * 또한 'sub' 이름으로 보관된 이메일도 반환하게 됩니다.
     * JWTUtil은 올바르게 생성되고, 검증이 가능한지 테스트를 진행하는 것이 좋습니다.
     * test 폴더 내에 security 패키지에 JWTTests 클래스를 작성합니다.
     *
     *
     * Jwt Token Example
     {
         "iat": 1689905187,
         "exp": 1692497187,
         "sub": "daeho.kang2@naver.com",
         "roles": "ROLE_ADMIN ROLES_USER"
     }
     */
    public JwtTokenPayloadDTO validateAndExtractAndReturnJwtTokenPayloadDTO(String tokenStr) throws Exception{
        JwtTokenPayloadDTO jwtTokenPayloadDTO = null;
        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            log.info("------------------------");
            String claim_member_id = claims.getSubject();
            Set<SimpleGrantedAuthority> claim_role_set = new HashSet<>();

            String roles = (String) claims.get("roles");
            String[] role_array = roles.split(" ");
            for (String authority : role_array) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
                claim_role_set.add(simpleGrantedAuthority);
            }

            jwtTokenPayloadDTO = JwtTokenPayloadDTO.builder()
                    .member_id(claim_member_id)
                    .member_role(claim_role_set)
                    .build();
        } catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            jwtTokenPayloadDTO = null;
        }
        return jwtTokenPayloadDTO;
    }

}
