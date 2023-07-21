package com.seminarhub.security;


import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;


public class JWTTests {
    private JWTUtil jwtUtil;

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * JWTTests 클래스는 스프링을 이용하는 테스트가 아니므로 내부에서 직접 JWTUTil 객체를 만들어서 사용합니다.
     */
    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore......");
        jwtUtil = new JWTUtil();
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * 만들어진 JWT 문자열을 확인할 수 있습니다. 상세정보는 https://jwt.io/ 에서 secretkey를 넣고 테스트하고, expiredate가 30일 후인지 확인합니다.(마우스 올려서 확인)
     */
    @DisplayName("JWTUTIL Generated Token Check")
    @Test
    public void testEncode() throws Exception{
        // given
        String member_id = "hello@hello.com";

        // when
        String str = jwtUtil.generateToken(member_id);

        // then
        System.out.println(str);
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * generateToken을 통해 생성한 email과 토큰값을 해석했을때의 email이 같은지 확인합니다.
     * 만약 expired된 토큰이라면  io.jsonwebtoken.ExpiredJwtException: 에러가 나옵니다.
     * @throws Exception
     */
    @DisplayName("JWTUTIL Generated Token Encoding")
    @Test
    public void testValidate() throws Exception{
        // given
        String member_id = "hello@hello.com";

        // when
        String str = jwtUtil.generateToken(member_id);
        Thread.sleep(5000);
        String resultEmail = jwtUtil.validateAndExtract(str);

        // then
        System.out.println(resultEmail);
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : generateTokenWithJwtTokenPayloadDTO TEST
     * JWT 값 테스트
     * 결과 : eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2ODk5Mjc2NDcsImV4cCI6 ...
     */
    @DisplayName("JWTUTil Generate Token Check")
    @Test
    public void testEncodeWithJwtTokenPayloadDTO() throws Exception{
        Set<SimpleGrantedAuthority> member_role_set = new HashSet<>();
        member_role_set.add(new SimpleGrantedAuthority("ROLE_USER"));
        member_role_set.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        JwtTokenPayloadDTO jwtTokenPayloadDTO = JwtTokenPayloadDTO.builder()
                .member_id("daeho.kang@naver.com")
                .member_role(member_role_set)
                .build();
        String str = jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
        System.out.println(str);
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : validateAndExtractAndReturnJwtTokenPayloadDTO TEST
     * 결과 : JwtTokenPayloadDTO(member_no=null, member_id=daeho.kang@naver.com, member_role=[ROLE_USER, ROLE_ADMIN])
     */
    @DisplayName("JWTUTIL Generated Token Encoding")
    @Test
    public void testValidateWithJwtTokenPayloadDTO() throws Exception{
        Set<SimpleGrantedAuthority> member_role_set = new HashSet<>();
        member_role_set.add(new SimpleGrantedAuthority("ROLE_USER"));
        member_role_set.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        JwtTokenPayloadDTO jwtTokenPayloadDTO = JwtTokenPayloadDTO.builder()
                .member_id("daeho.kang@naver.com")
                .member_role(member_role_set)
                .build();
        String str = jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
        Thread.sleep(5000);
        JwtTokenPayloadDTO resultJwtTokenPayloadDTO = jwtUtil.validateAndExtractAndReturnJwtTokenPayloadDTO(str);
        System.out.println(resultJwtTokenPayloadDTO);
    }


}