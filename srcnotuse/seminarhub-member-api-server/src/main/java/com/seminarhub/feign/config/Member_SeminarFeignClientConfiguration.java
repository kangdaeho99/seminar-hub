package com.seminarhub.feign.config;

import com.seminarhub.feign.handler.Member_SeminarFeignClientErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * [2023-08-30 daeho.kang]
 * Description: Configuration class for the Member_SeminarFeignClient.
 * This configuration provides additional settings and customizations for the Feign client.
 */
@Configuration
public class Member_SeminarFeignClientConfiguration {

    /**
     * [ 2023-08-30 daeho.kang ]
     * Description : Creates a request interceptor to add the JWT token from the request's Authorization header to Feign requests.
     *
     */
    @Bean
    public RequestInterceptor requsetInterceptor(){
        return template -> {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String jwtToken = request.getHeader("Authorization");

            if(jwtToken != null){
                template.header("Authorization", jwtToken);
            }
        };
    }

    /**
     * [ 2023-08-30 daeho.kang ]
     * Description :
     * Creates a custom ErrorDecoder for handling errors in the Feign client.
     */
    @Bean
    public ErrorDecoder errorDecoder(){
        return new Member_SeminarFeignClientErrorDecoder();
    }

//    @Bean
//    public Retryer retryer() {
//        return new Retryer.Default(3, 2000, 3); // 최대 3번까지 2초 간격으로 재시도
//    }

}


//public class Member_SeminarFeignClientConfiguration implements RequestInterceptor {
//
//    @Override
//    public void apply(RequestTemplate template) {
//        // 현재 요청의 HttpServletRequest 가져오기
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//
//        String jwtToken = request.getHeader("Authorization");
//
//        if(jwtToken != null)  {
//            template.header("Authorization", jwtToken);
//        }
//
//    }
//}
