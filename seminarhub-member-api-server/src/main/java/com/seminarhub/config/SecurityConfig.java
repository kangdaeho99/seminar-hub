package com.seminarhub.config;

import com.seminarhub.security.filter.ApiCheckFilter;
import com.seminarhub.security.filter.ApiLoginFilter;
import com.seminarhub.security.handler.ApiLoginFailHandler;
import com.seminarhub.security.handler.SeminarLogoutSuccessHandler;
import com.seminarhub.security.service.RedisRefreshTokenService;
import com.seminarhub.security.service.SeminarUserDetailsService;
import com.seminarhub.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * [ 2023-07-11 daeho.kang ]
 * Description : Security Config
 *
 */
@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Autowired
    private SeminarUserDetailsService seminarUserDetailsService;

    @Autowired
    private RedisRefreshTokenService refreshTokenService;

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : password 암호화에 사용되는 Bean 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/api/v1/member/test/**", jwtUtil());
    }

    /**
     * [ 2023-07-30 daeho.kang ]
     * Description : JwtUtil Bean 등록
     */
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * http.build() : 구성된 HttpSecurity 객체를 반환합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception{

        http.authorizeRequests().requestMatchers("/swagger-ui.html").permitAll();

        http.formLogin().disable();
        http.csrf().disable();
        http.logout().logoutUrl("/api/v1/member/logout").logoutSuccessHandler(new SeminarLogoutSuccessHandler(jwtUtil()));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(seminarUserDetailsService);

        //Filter 순서 조절 (패스워드 체크 이전 apiCheckFilter()) 실행되도록 설정
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        //AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(seminarUserDetailsService).passwordEncoder(passwordEncoder());
        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //반드시 필요
        http.authenticationManager(authenticationManager);

        //APILoginFilter
        ApiLoginFilter apiLoginFilter =  new ApiLoginFilter("/api/v1/member/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * 메모리상에 있는 데이터를 이용하는 인증매니저인 InMemoryUserDetailsManager 생성합니다.
     * 이를 통해 DB없이 메모리에 저장시켜서, 로그인할 수 있습니다. 임시의 아이디와 패스워드를 사용하기 위해 생성하는 함수입니다.
     * @return InMemoryUserDetailsManager(user); 메모리에 UserDetailsManager를 저장해주는 객체
     * UserDetailsService 등록시 삭제처리
     *
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        UserDetails user = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1111"))
                .roles("USER")
                .build();

        log.info("userDetailsService......................");
        log.info(user);

        return new InMemoryUserDetailsManager(user);
    }
    */


}
