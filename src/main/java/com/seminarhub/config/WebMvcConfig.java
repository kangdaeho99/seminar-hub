package com.seminarhub.config;

import com.seminarhub.security.Interceptor.CheckRoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * [ 2023-07-21 daeho.kang ]
 * Description : 
 * 
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final CheckRoleInterceptor checkRoleInterceptor;


    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : Interceptor 를 등록합니다.
     * CheckRoleInterceptor가 등록되어있습니다. EX) @CheckRole(roles = RoleType.USER)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(checkRoleInterceptor);
    }
}
