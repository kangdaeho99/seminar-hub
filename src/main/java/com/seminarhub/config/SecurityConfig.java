package com.seminarhub.config;

import com.seminarhub.security.filter.ApiCheckFilter;
import com.seminarhub.security.filter.ApiLoginFilter;
import com.seminarhub.security.handler.ApiLoginFailHandler;
import com.seminarhub.security.service.SeminarUserDetailsService;
import com.seminarhub.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * EnableMethodSecurity(prePostEnabled = true) : 어노테이션 기반의 접근 제한을 설정할 수 있도록 하는 설정입니다.
 * SecurityConfig를 사용해서 지정된 URL에 접근제한을 거는것은 번거로운 작업이기에,
 * EnableMethodSecurity의 적용, 접근 제한이 필요한 컨트롤러의 메서드에 @PreAuthorize 적용을 함으로써 적용합니다.
 * PreAuthorize를 이용하기 위해서 prePostEnabled = true 로 설정합니다.
 * PreAuthorized()의 value로는 문자열로 된 표현식을 넣을 수 있습니다.
 * 예시 1. @PreAuthorize("hasRole('ADMIN')")
 * 예시 2. @PreAuthorize("permitAll()")
 * 예시 3. @PreAuthorize("#authMember != null && #authMember.username eq \"hello@hello.com\"")
 * public String preauthorizeOnly(@AuthenticationPrincipal AuthMemberDTO authMember)
 */

@Configuration
@EnableWebSecurity
@Log4j2
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private SeminarUserDetailsService seminarUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

//    @Bean
//    public ApiLoginFilter apiLoginFilter() throws Exception{
////        Apl
//    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * http.build() : 구성된 HttpSecurity 객체를 반환합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception{

        http.authorizeRequests().requestMatchers("/swagger-ui.html").permitAll();

        http.formLogin();
        http.csrf().disable();
        http.logout().logoutUrl("/api/v1/logout");

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
        ApiLoginFilter apiLoginFilter =  new ApiLoginFilter("/api/v1/login", jwtUtil());
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
