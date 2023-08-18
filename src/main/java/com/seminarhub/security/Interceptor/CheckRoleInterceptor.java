package com.seminarhub.security.Interceptor;

import com.seminarhub.entity.RoleType;
import com.seminarhub.security.annotation.CheckRole;
import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;


/**
 * [ 2023-07-21 daeho.kang ]
 * Description : 권한처리 인터셉터입니다. ("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER")
 * WebMvcConfig를 통해 등록했습니다.
 */

@Component
@Log4j2
@RequiredArgsConstructor
public class CheckRoleInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * [ 2023-06-27 daeho.kang ]
     * Description : preHandler함수는 데이터가 들어오기전에 먼저 실행되는 함수입니다.
     * HandlerMethod ( @Controller, @RequestMapping ) 인지 확인합니다.
     * HandlerMethod에 @CheckRole 어노테이션이 존재하는지 확인합니다. 존재하지않는다면 현재 Interceptor를 종료시킵니다.
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("CheckRole Request URL: "+ request.getRequestURI());


        // ResourceHttpRequestHandler : handler가 ResourceHttpRequestHandler의 인스턴스인 경우, Swagger UI와 같은 정적 리소스 처리기
        // HandlerMethod : @Controller인지 확인
        if( handler instanceof HandlerMethod == false ) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CheckRole checkRole = handlerMethod.getMethodAnnotation(CheckRole.class);
        if(checkRole == null) return true;

        RoleType[] annotationRoleType = checkRole.roles();
        System.out.println("annotationRoleType:"+annotationRoleType);
        //실패했다면 종료
        if(checkAuthHeaderWithJwtTokenPayloadDTO(request, annotationRoleType) == false)  throw new AuthenticationException("No Roles");

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     *'Authorization' 헤더의 값을 확인하고 boolean 타입의 결과를 반환
     * authHeader Example
        header={alg=HS256},body={iat=1689910527, exp=1692502527, sub=daeho.kang@naver.com, roles=ROLE_USER ROLE_ADMIN },signature=tozivExke-glfw5lne0fjfC8Chs2mrB-KreYa9oX-ik
     */
    private boolean checkAuthHeaderWithJwtTokenPayloadDTO(HttpServletRequest request, RoleType[] roleType) {
        boolean checkResult = false;
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try {
                JwtTokenPayloadDTO jwtTokenPayloadDTO = jwtUtil.validateAndExtractAndReturnJwtTokenPayloadDTO(authHeader.substring(7));
                Set<SimpleGrantedAuthority> role_list = jwtTokenPayloadDTO.getMember_role();

                for(RoleType roleTypeValue : roleType){
                    if(role_list.contains(new SimpleGrantedAuthority("ROLE_"+roleTypeValue))){
                        checkResult = true;
                        break;
                    }
                }

                log.info("validate result: " + jwtTokenPayloadDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checkResult;
    }
}
