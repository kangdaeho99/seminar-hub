package org.example2.learningtest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class JUnitTest {
    @Autowired 
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
    static ApplicationContext contextObject = null;

    @Test 
    public void test1() {
        assertFalse(testObjects.contains(this));
        testObjects.add(this);
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test 
    public void test2() {
        assertFalse(testObjects.contains(this));
        testObjects.add(this);
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test 
    public void test3() {
        assertFalse(testObjects.contains(this));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
}
