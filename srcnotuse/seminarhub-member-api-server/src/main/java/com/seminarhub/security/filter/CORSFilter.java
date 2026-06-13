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
 * Description: CORS filter for handling Cross-Origin Resource Sharing (CORS) issues.
 * Since my project are to access APIs, CORS needs to be addressed to allow cross-origin requests.
 * There are various ways to handle CORS, including creating or configuring a CORS filter.
 *
 * This filter is configured with the highest precedence using @Order(Ordered.HIGHEST_PRECEDENCE)
 * to ensure it executes before other filters.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Set CORS headers to allow cross-origin requests
        response.setHeader("Access-Control-Allow-Origin", "*"); // Allow requests from any origin
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Allow credentials (e.g., cookies, authorization headers)
        response.setHeader("Access-Control-Allow-Methods", "*"); // Allow all HTTP methods
        response.setHeader("Access-Control-Max-Age", "3600"); // Cache pre-flight response for 3600 seconds
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization"); // Allow specified headers

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // Handle pre-flight requests by sending a successful response
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Continue with the filter chain for non pre-flight requests
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