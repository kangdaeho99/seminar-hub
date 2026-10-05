package org.example2.learningtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class PointCutTest {
    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException {
        String signature = "execution(public int org.example2.learningtest.Target.minus(int,int)"
                + " throws java.lang.RuntimeException)";
        checkExpression(signature, Target.class, "minus", true, int.class, int.class);
        checkExpression(signature, Target.class, "plus", false, int.class, int.class);
        checkExpression("execution(* *(..))", Target.class, "minus", true, int.class, int.class);
        checkExpression("execution(* *(..))", Bean.class, "method", true);
    }

    @Test
    public void methodNameAndArgumentsPointcut() throws NoSuchMethodException {
        // ()는 인자 없음, (*)는 인자 하나, (..)는 인자의 타입과 개수에 제한 없음이다.
        checkExpression("execution(* hello())", Target.class, "hello", true);
        checkExpression("execution(* hello())", Target.class, "hello", false, String.class);
        checkExpression("execution(* hello(*))", Target.class, "hello", false);
        checkExpression("execution(* hello(*))", Target.class, "hello", true, String.class);
        checkExpression("execution(* hello(..))", Target.class, "hello", true);
        checkExpression("execution(* hello(..))", Target.class, "hello", true, String.class);
        checkExpression("execution(* minus(..))", Target.class, "plus", false, int.class, int.class);
    }

    @Test
    public void typeAndPackagePointcut() throws NoSuchMethodException {
        String targetMethods = "execution(* org.example2.learningtest.Target.*(..))";
        checkExpression(targetMethods, Target.class, "method", true);
        checkExpression(targetMethods, Bean.class, "method", false);
        checkExpression("execution(* org.example2.learningtest.*.*(..))", Bean.class, "method", true);
        checkExpression("execution(* org.example2..*.*(..))", Target.class, "hello", true);

        // 인터페이스에 선언된 메소드만 선택하며 Target에만 있는 method()는 제외한다.
        String interfaceMethods = "execution(* org.example2.learningtest.TargetInterface.*(..))";
        checkExpression(interfaceMethods, Target.class, "hello", true);
        checkExpression(interfaceMethods, Target.class, "plus", true, int.class, int.class);
        checkExpression(interfaceMethods, Target.class, "method", false);
    }

    @Test
    public void expressionPointcutAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* sayH*(..))");
        checkAdviced(new HelloTarget(), pointcut, true);
    }

    private void checkExpression(String expression, Class<?> targetClass, String methodName,
            boolean expected, Class<?>... parameterTypes) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        Method method = targetClass.getMethod(methodName, parameterTypes);
        boolean matches = pointcut.getClassFilter().matches(targetClass)
                && pointcut.getMethodMatcher().matches(method, targetClass);
        assertEquals(expected, matches, expression + " -> " + method);
    }

    @Test
    public void classNamePointcutAdvisor() {
        //포인트컷 준비
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() { // 익명 내부 클래스 방식으로 클래스를 정의한다.
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        classMethodPointcut.setMappedName("sayH*"); // sayH로 시작하는 이름의 메소드만 선정한다.

        // 테스트
        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {}
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {}
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            // 클래스 조건을 통과해도 메소드 조건까지 만족해야 어드바이스가 적용된다.
            assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
            assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
            assertEquals("Thank You Toby", proxiedHello.sayThankYou("Toby"));
        } else {
            // 클래스 조건에서 제외되면 모든 메소드에 어드바이스가 적용되지 않는다.
            assertEquals("Hello Toby", proxiedHello.sayHello("Toby"));
            assertEquals("Hi Toby", proxiedHello.sayHi("Toby"));
            assertEquals("Thank You Toby", proxiedHello.sayThankYou("Toby"));
        }
    }

    interface Hello {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello {
        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank You " + name;
        }
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
