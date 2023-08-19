package com.seminarhub.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description :  RedisRefreshTokenService.class
 *  Redis를 이용하여 사용자의 Refresh Token을 저장, 조회, 삭제하는 기능을 제공하는 서비스 클래스입니다.
 */
@Service
public class RedisRefreshTokenService {

    @Autowired
    private RedisTemplate<String, String> redisRefreshTokenTemplate;

    // Refresh Token 저장
    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : 사용자의 RefreshToken을 등록합니다.
     */
    public void storeRefreshToken(String member_id, String refreshToken, long refreshTokenExpirationTime) {
        String redisKey = member_id;
        redisRefreshTokenTemplate.opsForValue().set(redisKey, refreshToken, refreshTokenExpirationTime, TimeUnit.MILLISECONDS);
    }

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : 사용자의 RefreshToken을 조회합니다.
     *
     */
    public String getRefreshToken(String member_id) {
        String redisKey = member_id;
        return redisRefreshTokenTemplate.opsForValue().get(redisKey);
    }

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description :사용자의 RefreshToken 삭제
     */
    public boolean deleteRefreshToken(String member_id) {
        String redisKey = member_id;
        return redisRefreshTokenTemplate.delete(redisKey);
    }



}
