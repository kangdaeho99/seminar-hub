package com.seminarhub.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [ 2023-08-19 daeho.kang ]
 * Description : Member Controller
 *
 */
@RestController
@RefreshScope
public class TestMemberController {

    @Value("${seminarhub.value}")
    private String configStr;

    @Value("${server.port}")
    private String configPortStr;

    /**
     * [ 2023-08-19 daeho.kang ]
     * Description : For Spring Cloud Config Test
     *
     */
    @GetMapping("/member/test")
    public String test(){
        return configStr;
    }

    @GetMapping("/member/test/port")
    public String testPortStr(){
        return configPortStr;
    }

}
