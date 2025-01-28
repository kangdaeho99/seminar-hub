package com.seminarhub.aop;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.annotation.UseGuard;
import com.seminarhub.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;

@Component
@Log4j2
public class UseGuardInterceptor implements HandlerInterceptor {
    @Autowired
    private final JWTUtil jwtUtil;

    public UseGuardInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Request URL: "+ request.getRequestURI());
        if( ( handler instanceof HandlerMethod) == false ) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UseGuard useGuard = handlerMethod.getMethodAnnotation(UseGuard.class);
        if(useGuard == null) return true;

        MemberDTO memberDTO = checkAuthHeaderWithJwtTokenPayloadDTO(request);
        if(memberDTO == null) throw new AuthenticationException("CANNOT LOGIN");
        request.setAttribute("currentUser", memberDTO);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private MemberDTO checkAuthHeaderWithJwtTokenPayloadDTO(HttpServletRequest request) {
        boolean checkResult = false;
        MemberDTO memberDTO = null;
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);
            try {
//                JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.validateAndExtractAndReturnJwtTokenPayloadDTO(authHeader.substring(7));
//                if(jwtTokenPayloadDTO.getId() != null) checkResult = true;
//                log.info("validate result: " + jwtTokenPayloadDTO);
//                checkResult = email.length() > 0;
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                if(email != null && !email.isEmpty()){
                    memberDTO = new MemberDTO();
                    memberDTO.setId(email);
                }
                log.info("validate result: " + email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return memberDTO;
    }
}
