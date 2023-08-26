package com.seminarhub.security.util;


import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * [ 2023-07-30 daeho.kang ]
 * Description :
 * JWTUtil은 SpringBoot의 Context 내에서 진행되는 테스트가 아니므로, SrpingBootTest가 없어도됩니다.
 *
 */
public class JWTTests {

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     */
    private final JWTUtil jwtUtil = new JWTUtil();

    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore......");
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * 만들어진 JWT 문자열을 확인할 수 있습니다. 상세정보는 https://jwt.io/ 에서 secretkey를 넣고 테스트하고, expiredate를 확인합니다.(마우스 올려서 확인)
     */
    @DisplayName("Test Generate Token")
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
     */
    @DisplayName("Test Encode Token")
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
    @DisplayName("Test Generate Token with JwtTokenPayloadDTO")
    @Test
    public void testEncodeWithJwtTokenPayloadDTO() throws Exception{
        // given
        JwtTokenPayloadDTO mockJwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();

        // when
        String str = jwtUtil.generateTokenWithJwtTokenPayloadDTO(mockJwtTokenPayloadDTO);

        // then
        System.out.println(str);
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : validateAndExtractAndReturnJwtTokenPayloadDTO TEST
     * 결과 : JwtTokenPayloadDTO(member_no=null, member_id=daeho.kang@naver.com, member_role=[ROLE_USER, ROLE_ADMIN])
     */
    @DisplayName("Test Encode Token With JwtTokenPayloadDTO")
    @Test
    public void testValidateWithJwtTokenPayloadDTO() throws Exception{
        // given
        JwtTokenPayloadDTO mockJwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();
        String str = jwtUtil.generateTokenWithJwtTokenPayloadDTO(mockJwtTokenPayloadDTO);
        Thread.sleep(5000);

        // when
        JwtTokenPayloadDTO resultJwtTokenPayloadDTO = jwtUtil.validateAndExtractAndReturnJwtTokenPayloadDTO(str);

        // then
        System.out.println(resultJwtTokenPayloadDTO);
    }


}