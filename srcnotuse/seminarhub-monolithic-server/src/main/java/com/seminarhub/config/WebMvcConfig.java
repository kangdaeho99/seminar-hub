package com.seminarhub.config;

import com.seminarhub.aop.CurrentUserArgumentResolver;
import com.seminarhub.aop.UseGuardInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * [ 2023-07-21 daeho.kang ]
 * Description : 
 * 
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UseGuardInterceptor useGuardInterceptor;

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    public WebMvcConfig(UseGuardInterceptor useGuardInterceptor, CurrentUserArgumentResolver currentUserArgumentResolver) {
        this.useGuardInterceptor = useGuardInterceptor;
        this.currentUserArgumentResolver = currentUserArgumentResolver;
    }

    /**
     * [ 2023-07-21 daeho.kang ]
     * Description : Interceptor 를 등록합니다.
     * useGuardInterceptor 등록되어있습니다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(useGuardInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(currentUserArgumentResolver);
    }
}
