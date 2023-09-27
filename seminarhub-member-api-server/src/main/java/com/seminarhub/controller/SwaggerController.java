package com.seminarhub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * [ 2023-07-30 daeho.kang ]
 * Description : Swagger에 등록하기 위해 Login, Logout URL을 등록
 * Security가 Filter에서 SecuriyConfig에 설정해둔 URL로 처리하기에 해당 Controller는 실제로는 처리 안됨
 */
@Controller // To Redirect TO Swagger UI, Need to Use COntroller ( Not Rest Controller , because Rest Returns the JSON Value)
@Log4j2
@RequiredArgsConstructor
@Tag(name = "1. Login & Logout API")
public class SwaggerController {

        /**
         * [ 2023-07-30 daeho.kang ]
         * Description : 로그인처리
         */
        @PostMapping("/api/v1/member/login")
        @Operation(summary = "1. 로그인, 접속아이디 : daeho.kang@naver.com / 패스워드 : 123123123")
        @Parameters({
                @Parameter(name = "member_id", description = "daeho.kang@naver.com 로 접속하세요", required = true),
                @Parameter(name = "member_password", description = "123123123  으로 접속하세요", required = true)
        })
        public void Login(@RequestParam("member_id") String member_id, @RequestParam("member_password") String member_password) {

        }

        /**
         * [ 2023-07-30 daeho.kang ]
         * Description : 로그아웃 처리
         */
        @PostMapping("/api/v1/member/logout")
        @Operation(summary = "2. 로그아웃")
        public void fakeLogout() {

        }

        @GetMapping("/api/v1/member/swagger-ui")
        public String redirectToSwagger(){
                return "redirect:/swagger-ui.html";
        }

}
