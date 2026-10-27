package org.example2.learningtest.spring.pointcut;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example2.learningtest.Bean;
import org.example2.learningtest.Target;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointCutTest {

    @Test 
    public void test() throws NoSuchMethodException{
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
    }

    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int "
                + "org.example2.learningtest.Target.minus(int,int) "
                + "throws java.lang.RuntimeException)");

        // Target.minus(): 클래스 필터와 메소드 매처를 모두 통과한다.
        assertTrue(pointcut.getClassFilter().matches(Target.class)
                && pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("minus", int.class, int.class), Target.class));

        // Target.plus(): 클래스는 일치하지만 메소드 조건이 일치하지 않는다.
        assertFalse(pointcut.getClassFilter().matches(Target.class)
                && pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("plus", int.class, int.class), Target.class));

        // Bean.method(): 클래스 필터에서 제외된다.
        assertFalse(pointcut.getClassFilter().matches(Bean.class)
                && pointcut.getMethodMatcher().matches(
                        Bean.class.getMethod("method"), Bean.class));
    }

}
