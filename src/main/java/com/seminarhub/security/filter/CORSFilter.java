package com.seminarhub.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * CORS 필터처리
 * 내 프로젝트는 ajax를 활용하여 API를 사용하려고 하기에 CORS(Corss-origin Resource Sharing) 문제를 해결해야합니다.
 * CORS 처리를 위한 필터를 만들거나 설정하는 방법은 여러가지가 존재합니다.
 *
 * Order(Ordered.HIGHEST_PRECEDENCE) : 모든 필터중에서 가장 먼저 동작하도록 작업합니다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        } else{
            filterChain.doFilter(request, response);
        }
    }
}



/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
     axios로 외부에서 API 호출 예시
     $(".btn).click(function(){
         $.ajax({
            beforeSend: function(request){
                request.setRequestHeader("Authorization", 'Bearer ' + jwtValue);
                //jwtValue는 JWT값입니다.
            },
            dataType: "json",
            url: 'http://localhost:8080/room/all',
            data : {email: 'hello1@hello.com'},
            success : function(arr){
                console.log(arr);
             }
          });
     })
 */