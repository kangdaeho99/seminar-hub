package com.seminarhub.config;

import com.seminarhub.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * [ 2023-07-11 daeho.kang ]
 * Description : Security Config
 *
 */
@Configuration
@Log4j2
public class SecurityConfig {

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description: Register JWTUtil as a Bean
     */
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description:
     * Configures security filters to handle authentication and authorization.
     * SeminarService doesn't require login/logout functionality, so unnecessary features are disabled.
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
