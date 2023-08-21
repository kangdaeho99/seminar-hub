package com.seminarhub.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [ 2023-08-19 daeho.kang ]
 * Description : Spring Cloud Config TestController
 * Seminar의 Config가 잘연결되었는지 확인하기 위한 컨트롤러입니다.
 * 아래의 API호출을 통하여 해당 JVM의 세팅값을 알 수 있습니다.
 */
@RestController
@RefreshScope
public class TestSeminarController {

    @Value("${seminarhub.value}")
    private String configStr;

    @Value("${server.port}")
    private String configPortStr;
    /**
     * [ 2023-08-19 daeho.kang ]
     * Description : For Spring Cloud Config Test
     *
     */
    @GetMapping("/seminar/test")
    public String test(){
        return configStr;
    }

    @GetMapping("/seminar/test/port")
    public String testPortStr(){
        return configPortStr;
    }
}
