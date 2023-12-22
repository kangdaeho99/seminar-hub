package com.seminarhub.service;

import com.seminarhub.aop.LogAdvice;
import com.seminarhub.repository.SeminarQuerydslRepository;
import com.seminarhub.repository.SeminarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description: Test class for SeminarServiceImpl
 */
//@SpringBootTest(classes = {SeminarServiceImpl.class, SeminarQuerydslRepository.class, LogAdvice.class})
@SpringBootTest
public class SeminarServiceTests {
    @MockBean
    private SeminarRepository seminarRepository;

    @MockBean
    private SeminarQuerydslRepository seminarQuerydslRepository;

    @Autowired
    private SeminarService seminarService;

    @Test
    public void testClass(){
        System.out.println(seminarService.printOutString("helloAOP"));
        System.out.println(seminarService.printOutString(""));
    }

}
