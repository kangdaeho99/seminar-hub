package com.seminarhub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@Tag(name = "1. Login & Logout API")
public class SwaggerController {

        @PostMapping("/api/v1/login")
        @Operation(summary = "1. 로그인, 접속아이디 : daeho.kang@naver.com / 패스워드 : 123123123")
        @Parameters({
                @Parameter(name = "member_id", description = "daeho.kang@naver.com 로 접속하세요", required = true),
                @Parameter(name = "member_password", description = "123123123  으로 접속하세요", required = true)
        })
        public void Login(@RequestParam("member_id") String member_id, @RequestParam("member_password") String member_password) {

        }

        @GetMapping("/api/v1/logout")
        @Operation(summary = "2. 로그아웃")
        public void fakeLogout() {

        }

}
