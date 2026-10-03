package org.example2.learningtest;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class JUnitTest {
    private static final Set<JUnitTest> testObjects = new HashSet<>();

    @Test 
    public void test1() {
        assertFalse(testObjects.contains(this), "각 테스트는 새로운 객체에서 실행되어야 한다");
        testObjects.add(this);
    }

    @Test 
    public void test2() {
        assertFalse(testObjects.contains(this), "각 테스트는 새로운 객체에서 실행되어야 한다");
        testObjects.add(this);
    }

    @Test 
    public void test3() {
        assertFalse(testObjects.contains(this), "각 테스트는 새로운 객체에서 실행되어야 한다");
        testObjects.add(this);
    }
}
