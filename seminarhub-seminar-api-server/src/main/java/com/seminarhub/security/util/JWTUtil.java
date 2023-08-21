package com.seminarhub.security.util;

import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.service.RedisRefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * JWTUtil은 스프링 환경이 아닌곳에서도 사용할 수 있도록 유틸리티 클래스로 설계합니다.
 */
@Log4j2
public class JWTUtil {

    @Autowired
    private RedisRefreshTokenService redisRefreshTokenService;

    private String secretKey = "secret";

    // 12 Hours
    private long accessTokenExpirationTime = 43200000;

    // 7 days
    private long refreshTokenExpirationTime = 604800000;

    //1 month
    private long expire = 60 * 24 * 30;

    /**
     * [ 2023-07-28 daeho.kang ]
     * Description : Test 시에 사용되는 객체 생성
     * Test시에 Token 발급이 필요한 코드가 많기에, JwtUtil에 생성
     */
    public JwtTokenPayloadDTO mockJwtTokenPayloadDTO() {
        Set<SimpleGrantedAuthority> member_role_set = new HashSet<>();
        member_role_set.add(new SimpleGrantedAuthority("ROLE_USER"));
        member_role_set.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        JwtTokenPayloadDTO jwtTokenPayloadDTO = JwtTokenPayloadDTO.builder()
                .member_id("daeho.kang@naver.com")
                .member_role(member_role_set)
                .build();
        return jwtTokenPayloadDTO;
    }

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
     }
     */
    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
//                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(Long.parseLong(accessTokenExpirationTime)).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : JwtTokenPayloadDTO와 함께 JWT토큰을 생성하는 역할을 합니다.
     * JWT 문자열 자체를 알면 누구든 API를 사용할 수 있다는 문제가 생기므로 만료기간(expire)값을 설정하고 'secret' 이라는 key를 이용해서 Signature 를 생성합니다.
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
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(accessTokenExpirationTime).toInstant()))
                .claim("sub", jwtTokenPayloadDTO.getMember_id())
                .claim("roles", jwtTokenPayloadDTO.getMember_Role_ToString())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    /**
     * [ 2023-07-26 daeho.kang ]
     * Description : Regsiter Refresh Token In Redis With JwtTokenPayloadDTO
     * JWT를 새로 발급받는 기능이 존재할 것으로 예상되어, Redis에 Refresh Token 넣는 함수를 따로 생성합니다.
     */
    public String generateRefreshTokenWithJwtTokenPayloadDTO(JwtTokenPayloadDTO jwtTokenPayloadDTO) throws Exception{

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(refreshTokenExpirationTime).toInstant()))
                .claim("sub", jwtTokenPayloadDTO.getMember_id())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();

        return refreshToken;

    }

    /**
     * [ 2023-07-26 daeho.kang ]
     * Description : Regsiter Refresh Token In Redis With JwtTokenPayloadDTO
     * JWT를 새로 발급받는 기능이 존재할 것으로 예상되어, Redis에 Refresh Token 넣는 함수를 따로 생성합니다.
     */
    public void setRefreshTokenInRedisWithJwtTokenPayloadDTO(JwtTokenPayloadDTO jwtTokenPayloadDTO, String refreshToken) throws Exception{
        redisRefreshTokenService.storeRefreshToken(jwtTokenPayloadDTO.getMember_id(), refreshToken, refreshTokenExpirationTime);
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

    /**
     * [ 2023-07-26 daeho.kang ]
     * Description : Access Token이 Expired되었는지 확인합니다. || Expired 되었다면 True, 아니라면 False Return
     * 사용예시 )
     * JwtTokenInterceptor.java AccessToken의 Expired 확인
     * CheckRoleInterceptor.java AccessToken의 Expired 확인
     *
     */
    public boolean isTokenExpired(String token) {
        Claims tokenClaims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        Date expirationDate = tokenClaims.getExpiration();
        Date currentDate = new Date();

        return expirationDate.before(currentDate);
    }

    /**
     * [ 2023-07-28 daeho.kang ]
     * Description : Redis의 RefreshToken Expired되었는지 확인합니다. || Expired 되었다면 True, 아니라면 False Return
     * 사용예시 )
     * - JwtTokenInterceptor.java RedisToken이 Expired 확인하는 용도
     *
     */
    public boolean isRedisRefreshTokenExpired(String member_id){
        String refreshToken = redisRefreshTokenService.getRefreshToken(member_id);
        Claims tokenClaims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(refreshToken)
                .getBody();

        Date expirationDate = tokenClaims.getExpiration();
        Date currentDate = new Date();

        return expirationDate.before(currentDate);
    }

    public boolean deleteRedisRefreshToken(String member_id){
        return redisRefreshTokenService.deleteRefreshToken(member_id);
    }

}
