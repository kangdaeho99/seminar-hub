package com.seminarhub.security;

import com.seminarhub.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTTests {
    private JWTUtil jwtUtil;
    
    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore...........");
        jwtUtil = new JWTUtil();
    }
    
    @Test
    public void testEncode() throws Exception{
        String email = "passionfruit200@email.com";
        String str = jwtUtil.generateToken(email);
        System.out.println(str);
    }

    @Test
    public void testValidate() throws Exception{
        String email = "passionfruit200@email.com";
        String str = jwtUtil.generateToken(email);
        Thread.sleep(5000);
        System.out.println(str);
        String resultEmail = jwtUtil.validateAndExtract(str);
        System.out.println(resultEmail);
    }
}
