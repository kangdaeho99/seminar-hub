package com.seminarhub.security.service;

import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : RedisRefreshTokenService를 Tests 하는 클래스입니다.
 *
 */
@SpringBootTest(classes = {RedisRefreshTokenService.class})
public class RedisRefreshTokenServiceTests {

    @MockBean
    private RedisTemplate<String, String> redisRefreshTokenTemplate;

    @Autowired
    private RedisRefreshTokenService redisRefreshTokenService;

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description :
     * RedisRefreshTokenSevice Class만 테스트하기에 Bean으로 가져오지 못하여, 아래와 같이 생성
     * 만약 @Autowired로 가지고오고 싶을시 JWTUtil.class를 추가
     */
    private final JWTUtil jwtUtil = new JWTUtil();

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : registerRedisRefreshTokenTest
     * 사용자의 refreshToken을 등록합니다.
     * Mock을 활용해 RedisTemplate의 opsForValue() 값을 가짜로 생성해 테스트합니다.
     * verify()를 활용해 실제로 valueOpsMock 객체의 set() 메서드가 파라미터들과 함께 호출되었는지를 검증합니다.
     */
    @DisplayName("Test RedisRefreshTokenService Store")
    @Test
    public void storeRedisRefreshTokenTest() throws Exception{
        // given
        String member_id = "daeho.kang@hello.com";
        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();
        String token = jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
        ValueOperations<String, String> valueOpsMock = mock(ValueOperations.class);
        when(redisRefreshTokenTemplate.opsForValue()).thenReturn(valueOpsMock);

        // when
        redisRefreshTokenService.storeRefreshToken(member_id, token, 10000);

        // then
        verify(valueOpsMock).set(eq(member_id), eq(token), eq(10000L), eq(TimeUnit.MILLISECONDS));
    }

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : getRedisRefreshTokenTest
     * 사용자의 refreshToken 의 값을 잘 가져오는지 체크합니다.
     */
    @DisplayName("Test RedisRefreshTokenService get")
    @Test
    public void getRedisRefreshTokenTest() throws Exception{
        // given
        String member_id = "daeho.kang@hello.com";
        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();
        String token = jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);

        //redisTemplate의 valueOps를 Mock 객체로 생성하여 테스트 진행
        ValueOperations<String, String> valueOpsMock = mock(ValueOperations.class);
        when(redisRefreshTokenTemplate.opsForValue()).thenReturn(valueOpsMock);
        when(valueOpsMock.get(member_id)).thenReturn(token);

        // when
        String resultToken = redisRefreshTokenService.getRefreshToken(member_id);

        // then
        Assertions.assertEquals(token,resultToken);
        verify(redisRefreshTokenTemplate.opsForValue()).get(member_id);
    }

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : deleteRedisRefreshToken
     * 사용자의 Refresh Token을 삭제하는 기능 테스트
     */
    @DisplayName("Test RedisRefreshTokenService delete")
    @Test
    public void deleteRedisRefreshToken() throws Exception{
        // given
        String member_id = "daeho.kang@hello.com";
        when(redisRefreshTokenTemplate.delete(member_id)).thenReturn(true);

        // when
        boolean result = redisRefreshTokenService.deleteRefreshToken(member_id);

        // then
        Assertions.assertEquals(result, true);
        verify(redisRefreshTokenTemplate).delete(member_id);
    }



}
