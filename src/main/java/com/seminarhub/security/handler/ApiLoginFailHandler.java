package com.seminarhub.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * [ 2023-06-27 daeho.kang ]
 * Description :
 * ApiLoginFilter로 직접 인증처리를 한뒤 인중 후 성공 혹은 실패에 따른 처리를 위한 클래스입니다.
 */
@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {


    /**
     * [ 2023-07-11 daeho.kang ]
     * Description :
     * ApiLoginFilter에서 인증에 실패하는 경우 API 서버는 일반 화면이 아니라 JSON 결과가 전송되도록 수정해야합니다.
     * 성공하는 경우에는 클라이언트가 보관할 인증 토큰이 전송되어야합니다.
     * AbstractAuthenticationProcessingFilter에는 setAuthenticationFailureHandler()를 이용해서 인증에 실패했을경우 처리를 지정할 수 있습니다.
     * ApiLoginFailHandler 는 AuthenticationHandler 인터페이스를 구현하는 클래스로 오직 인증에 실패하는 경우에 처리를 전담하도록 구성합니다.
     * 인증에 실패하면 '401' 상태코드를 반환합니다.
     * SecurityConfig에서 사용하기 위해 ApiLoginFilter의 setAuthenticationFailureHandler()를 적용해주어야합니다.
     *
     *
     * @param request the request during which the authentication attempt occurred.
     * @param response the response.
     * @param exception the exception which was thrown to reject the authentication
     * request.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login fail handler...........");
        log.info(exception.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //json return
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = exception.getMessage();
        json.put("code", "401");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
