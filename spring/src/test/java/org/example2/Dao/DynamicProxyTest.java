package org.example2.Dao;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicProxyTest {
    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget(); //타깃은 인터페이스를 통해 접근하는 습관을 들이자.
        assertEquals("Hello Toby", hello.sayHello("Toby"));
        assertEquals("Hi Toby", hello.sayHi("Toby"));
        assertEquals("Thank You Toby", hello.sayThankYou("Toby"));
    }

    @Test
    public void dynamicProxy() {
        // JDK 다이내믹 프록시는 인터페이스를 지정하고, 핸들러가 타깃을 보관한다.
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[] { Hello.class },
                new UppercaseHandler(new HelloTarget()));

        assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
        assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        assertEquals("THANK YOU TOBY", proxiedHello.sayThankYou("Toby"));
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); // 타깃의 Hello 인터페이스를 자동으로 감지한다.
        pfBean.addAdvice(new UppercaseAdvice()); // 부가기능은 타깃과 별도로 등록한다.

        // 컨테이너 없이 팩토리를 직접 생성했으므로 getObject()로 프록시를 가져온다.
        Hello proxiedHello = (Hello) pfBean.getObject();
        assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
        assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        assertEquals("THANK YOU TOBY", proxiedHello.sayThankYou("Toby"));
    }

    @Test
    public void shareAdviceAcrossDifferentTargets() {
        UppercaseAdvice sharedAdvice = new UppercaseAdvice();

        ProxyFactoryBean helloFactory = new ProxyFactoryBean();
        helloFactory.setTarget(new HelloTarget());
        helloFactory.addAdvice(sharedAdvice);
        Hello helloProxy = (Hello) helloFactory.getObject();

        ProxyFactoryBean welcomeFactory = new ProxyFactoryBean();
        welcomeFactory.setTarget(new HelloTarget() {
            @Override
            public String sayHello(String name) {
                return "Welcome " + name;
            }
        });
        welcomeFactory.addAdvice(sharedAdvice); // 같은 어드바이스 인스턴스를 재사용한다.
        Hello welcomeProxy = (Hello) welcomeFactory.getObject();

        // 각 호출의 MethodInvocation이 타깃을 구분하므로 서로의 타깃을 덮어쓰지 않는다.
        assertEquals("HELLO TOBY", helloProxy.sayHello("Toby"));
        assertEquals("WELCOME TOBY", welcomeProxy.sayHello("Toby"));
        assertEquals("HELLO TOBY", helloProxy.sayHello("Toby"));
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 타깃 필드 없이 호출마다 전달받는 MethodInvocation으로 다음 처리를 진행한다.
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut(); // 메소드 이름을 비교해서 대상을 선정하는 알고리즘을 제공하는 포인트컷 생성
        pointcut.setMappedName("sayH*"); // 이름 비교 조건 설정. sayH 로 시작하는 모든 메소드를 선택하게 한다.

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice())); // 포인트컷과 어드바이스를 Advisor로 묶어서 한번에 추가

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
        assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        assertEquals("Thank You Toby", proxiedHello.sayThankYou("Toby")); // 메소드 이름이 포인트컷의 선정조건에 맞지 않으므로, 부가기능(대문자변환)이 적용되지 않는다.
    }
}
