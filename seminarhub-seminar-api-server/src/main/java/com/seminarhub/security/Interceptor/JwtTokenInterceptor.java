package com.seminarhub.security.Interceptor;

import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : JWtTokenInterceptor
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * [ 2023-07-26 daeho.kang ]
     * Description :
     * Jwt Token을 가지고서 접근한다면,
     *      JWTToken이 Expired되었다면 : Redis 의 RefreshToken이 존재여부 확인하고 존재한다면, accessToken을 재발급
     *      JWTToken이 Expired되지 않았다면 : 발급 X
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            //만약 Access 토큰이 유효하지 않다면, Redis에서 해당 member_id값이 존재하는지 확인
            if(jwtUtil.isTokenExpired(authHeader.substring(7)) == true){
                JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.validateAndExtractAndReturnJwtTokenPayloadDTO(authHeader.substring(7));
                String member_id = jwtTokenPayloadDTO.getMember_id();

                //만약 Redis의 Refresh Token 이 유효하다면, 들어온 Token 정보로 다시 토큰 재발급
                if(jwtUtil.isRedisRefreshTokenExpired(member_id) == true){
                    String accessToken = null;
                    String refreshToken = null;
                    try{
                        accessToken = jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
                        refreshToken = jwtUtil.generateRefreshTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
                        jwtUtil.setRefreshTokenInRedisWithJwtTokenPayloadDTO(jwtTokenPayloadDTO, refreshToken);
                        response.setContentType("text/plain");
                        response.getOutputStream().write(accessToken.getBytes());
                        log.info(accessToken);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
