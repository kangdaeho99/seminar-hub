package com.seminarhub.config;

import com.seminarhub.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


/**
 * [ 2023-07-11 daeho.kang ]
 * Description : Security Config
 *
 */
@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : JwtUtil Bean 등록
     */
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description :
     * MemberSeminarService 에서는 Security에 대한 로그인/로그아웃 기능을 사용하지 않으므로 불필요한 기능은 모두 제거합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception{

        http.authorizeRequests().requestMatchers("/swagger-ui.html").permitAll();

        http.httpBasic().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
