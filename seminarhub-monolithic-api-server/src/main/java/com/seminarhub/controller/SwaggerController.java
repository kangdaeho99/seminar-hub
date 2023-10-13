package com.seminarhub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * [ 2023-07-30 daeho.kang ]
 * Description : Swagger에 등록
 *
 */
@Controller // To Redirect TO Swagger UI, Need to Use COntroller ( Not Rest Controller , because Rest Returns the JSON Value)
@Log4j2
@RequiredArgsConstructor
//@Tag(name = "1. Login & Logout API")
public class SwaggerController {

        @GetMapping("/api/v1/seminar/swagger-ui")
        public String redirectToSwagger(){
                return "redirect:/swagger-ui.html";
        }

}
