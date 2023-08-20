package com.seminarhub.security.filter;

import com.seminarhub.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("REQUEStURI:" + request.getRequestURI());
        log.info(antPathMatcher.match(pattern, request.getRequestURI()));
        if(antPathMatcher.match(pattern, request.getRequestURI())){
            log.info("--------------------------APICheckFilter-------------------------------");

            boolean checkHeader = checkAuthHeader(request);

            if(checkHeader){
                filterChain.doFilter(request, response);
                return;
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴 및 한글깨짐 수정
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     *'Authorization' 헤더의 값을 확인하고 boolean 타입의 결과를 반환
     */
    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try {
                String member_id = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + member_id);
                checkResult =  member_id.length() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }

//            if(authHeader.equals("12345678")){
//                checkResult = true;
//            }
        }

        return checkResult;
    }
}
