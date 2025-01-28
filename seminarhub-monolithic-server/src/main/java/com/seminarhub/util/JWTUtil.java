package com.seminarhub.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * JWTUtil은 스프링 환경이 아닌곳에서도 사용할 수 있도록 유틸리티 클래스로 설계합니다.
 */
@Log4j2
public class JWTUtil {

    private String secretKey = "passionfruit200";

    //1month
    private long expire = 60 * 24 * 30;

    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public String validateAndExtract(String tokenStr) throws Exception{
        String contentValue = null;
        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            log.info("-------------------------");
            contentValue = claims.getSubject();
        } catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }


}
