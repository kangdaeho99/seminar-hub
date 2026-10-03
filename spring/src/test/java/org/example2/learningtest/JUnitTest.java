package org.example2.learningtest;

import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class JUnitTest {
    // static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
    static JUnitTest testObject;

    @Test 
    public void test1() {
        assertNotSame(this, testObject);
        testObject = this;
    }

    @Test 
    public void test2() {
        assertNotSame(this, testObject);
        testObject = this;
    }

    @Test 
    public void test3() {
        assertNotSame(this, testObject);
        testObject = this;
    }
}
