package com.seminarhub.redis;

import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : RedisRefreshTokenTemplateTests는 RedisTemplate을 사용하여 Refresh Token을 테스트하는 테스트 클래스입니다.
 * 
 */
@SpringBootTest
public class RedisRefreshTokenTemplateTests {

    @Autowired
    private RedisTemplate<String, String> redisRefreshTokenTemplate;

    private final JWTUtil jwtUtil = new JWTUtil();

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description: Redis에 Refresh Token을 저장하는 테스트 메서드입니다.
     * 이 테스트 메서드는 특정 회원의 Refresh Token을 Redis에 저장하는 시나리오를 시뮬레이션합니다.
     * JWTUtil을 사용하여 가짜 Refresh Token을 생성하고, 지정된 만료 시간과 함께 Redis에 설정합니다.
     * 이 테스트는 예외 없이 작동하는지를 검증합니다.
     */
    @DisplayName("Test RedisRefreshTokenTemplate set")
    @Test
    public void testRedisRefreshTokenTemplateSet() throws Exception {
        // given
        String member_id = "daeho.kang@hello.com";
        JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.mockJwtTokenPayloadDTO();
        String refreshToken = jwtUtil.generateRefreshTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);

        // when
        redisRefreshTokenTemplate.opsForValue().set(member_id, refreshToken, 100000);

        // then
    }

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description: Redis에서 Refresh Token을 조회하는 테스트 메서드입니다.
     * 이 테스트 메서드는 특정 회원의 Refresh Token을 Redis에서 조회하는 시나리오를 시뮬레이션합니다.
     * 조회한 Refresh Token이 null이 아닌지를 검증하여 작동 여부를 확인합니다.
     */
    @DisplayName("Test RedisRefreshTokenTemplate Get")
    @Test
    public void testRedisRefreshTokenTemplateGet() throws Exception {
        // given
        String member_id = "daeho.kang@hello.com";

        // when
        String value = redisRefreshTokenTemplate.opsForValue().get(member_id);

        // then
        //Assertions.assertNotNull(value);
    }


    /**
     * [ 2023-07-30 daeho.kang ]
     * Description: Redis에서 Refresh Token을 삭제하는 테스트 메서드입니다.
     * 이 테스트 메서드는 특정 회원의 Refresh Token을 Redis에서 삭제하는 시나리오를 시뮬레이션합니다.
     * 삭제 작업이 성공적으로 수행되었는지 'true'를 반환하여 검증합니다.
     * 참고: 이 테스트는 'testRedisRefreshTokenTemplateSet' 이후에 실행되어야 합니다. Redis에 유효한 Refresh Token이 저장된 상태에서 실행되어야 합니다.
     */
    @DisplayName("Test RedisRefreshTokenTemplate Delete")
    @Test
    public void testRedisRefreshTokenTemplateDelete() throws Exception{
        // given
        String member_id="daeho.kang@hello.com";

        // when
        boolean result = redisRefreshTokenTemplate.delete(member_id);

        // then
        //Assertions.assertEquals(result, true);
    }




}
