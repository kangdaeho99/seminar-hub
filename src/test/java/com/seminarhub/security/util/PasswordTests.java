package com.seminarhub.security.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * [ 2023-07-28 daeho.kang ]
     * Description : PasswordEcndoer Test
     */
    @DisplayName("Test PasswordEncoder")
    @Test
    public void testEncode(){
        String password = "1111";

        String enPw = passwordEncoder.encode(password);

        System.out.println("enPw: "+enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);

        System.out.println("matchResult: " + matchResult);
    }

}
