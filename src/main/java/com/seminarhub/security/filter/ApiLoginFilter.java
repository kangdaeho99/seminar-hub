package com.seminarhub.security.filter;

import com.seminarhub.security.dto.JwtTokenPayloadDTO;
import com.seminarhub.security.dto.MemberAuthDTO;
import com.seminarhub.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : Filter에서 로그인처리 ( extends AbstractAuthenticationProcessingFilter )
 * UsernamePasswordAuthenticationFilter 를 대체함
 */
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * JWTUtil의 생성과 검증(validation)에 문제가 없다면 최종적으로 ApiLoginFilter/ApiCheckFilter에 적용해야합니다.
     */
    private JWTUtil jwtUtil;

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * @param defaultFilterProcessesUrl
     * @param jwtUtil : APILoginFilter는 JWTUTIL을 생성자를 통해서 주입받습니다.
     */
    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : 로그인 인증함수입니다.
     * UsernamePasswordAuthenticationToken 을 생성하여, AuthenticateManager에 전송하여 로그인 요청합니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("------------------ApiLoginFilter------------------------");
        log.info("attemptAuthentication");

        String member_id = request.getParameter("member_id");
        String member_password = request.getParameter("member_password");

        System.out.println("MEMBER_ID"+member_id);
        System.out.println("MEMBER_PW"+member_password);

        if(member_id == null){
            throw new BadCredentialsException("member_id cannot be null");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(member_id, member_password);

        return getAuthenticationManager().authenticate(authToken);
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description : Login 성공시 실행되고, JWTToken을 생성하여 Response에 등록합니다.
     * 인증 성공을 처리합니다.(ApiLoginFailHandler 처럼 따로 클래스를 만들어처리할 수 있지만, AbstractAuthenticationProcessingFilter 클래스에 존재하는
     * successfulAuthentication() 메서드를 override해서 구현했습니다.
     * @param authResult the object returned from the <tt>attemptAuthentication</tt> : 성공한 사용자의 인증정보를 가지고 있는 Authentication 객체.
     *                   이를 통해 인증에 성고한 사용자의 정보를 로그에서 확인합니다. '/api/login?member_id=ewqeqw&&member_password=1111'
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException{
        log.info("---------------------ApiLoginFilter------------------------");
        log.info("successfulAuthentication: "+ authResult);

        log.info(authResult.getPrincipal());

//        String member_id = ((MemberAuthDTO) authResult.getPrincipal()).getUsername();
        MemberAuthDTO memberAuthDTO = (MemberAuthDTO) authResult.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = memberAuthDTO.getAuthorities();
        Set<SimpleGrantedAuthority> simpleAuthorities = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }

        JwtTokenPayloadDTO jwtTokenPayloadDTO = JwtTokenPayloadDTO
                .builder()
                .member_id(memberAuthDTO.getMember_id())
                .member_role(simpleAuthorities)
                .build();
        String token = null;
        try{
            token = jwtUtil.generateTokenWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
            jwtUtil.generateRefreshTokenInRedisWithJwtTokenPayloadDTO(jwtTokenPayloadDTO);
            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());
            log.info(token);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
