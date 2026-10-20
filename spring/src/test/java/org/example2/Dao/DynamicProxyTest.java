package org.example2.Dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicProxyTest {
    
    @Test 
    public void simpleProxy() {
        Hello hello = new HelloTarget(); //타깃은 인터페이스를 통해 접근하는 습관을 들이자.
        assertEquals("Hello Toby", hello.sayHello("Toby"));
    }
}
