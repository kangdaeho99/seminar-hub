package com.seminarhub.security.handler;

import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : 'logout' URL에 Logout 요청들어왔을때 작업처리
 *
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class SeminarLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JWTUtil jwtUtil;

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : Logout 성공 후 원하는 작업 처리
     * 작업 1. 해당 Member의 JWTToken과 함께 들어온다면, 해당 Member의 Redis RefreshToken을 삭제합니다.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);
            try{
                JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.validateAndExtractAndReturnJwtTokenPayloadDTO(authHeader.substring(7));
                System.out.println("-------jwtTokenPayladDTOCHECK:"+jwtTokenPayloadDTO);
                jwtUtil.deleteRedisRefreshToken(jwtTokenPayloadDTO.getMember_id());
            } catch(Exception e){
                e.printStackTrace();
            }

            //json return
            response.setContentType("application/json;charset=utf-8");
            JSONObject json = new JSONObject();
            json.put("code", "200");
            json.put("message", "Logout Success");
            PrintWriter out = response.getWriter();
            out.print(json);
        }
    }
}
