package org.example2.Dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class FactoryBeanTest {

    @Autowired 
    ApplicationContext context;

    @Test 
    public void getMessageFromFactoryBean() {
        Object bean = context.getBean("message");
        Message message = assertInstanceOf(Message.class, bean);
        assertEquals("Factory Bean", message.getText());
    }

    @Test 
    public void getFactoryBean() {
        Object factory = context.getBean("&message");
        assertInstanceOf(MessageFactoryBean.class, factory);
    }

    @Test
    public void getAutomaticallyCreatedTransactionProxy() {
        Object service = context.getBean("userService");
        assertInstanceOf(UserService.class, service);
        assertTrue(AopUtils.isJdkDynamicProxy(service));
        Advised proxy = assertInstanceOf(Advised.class, service);
        assertSame(context.getBean("transactionAdvisor"), transactionAdvisor(proxy));
        assertThrows(BeanIsNotAFactoryException.class, () -> context.getBean("&userService"));
        assertTrue(UserService.class.isAssignableFrom(context.getType("userService")));
        assertEquals(UserServiceImpl.class, AopUtils.getTargetClass(service));
        assertFalse(context.containsBean("userServiceImpl"));
    }

    @Test
    public void reuseTransactionAdviceForCoreService() {
        Object service = context.getBean("coreService");
        assertInstanceOf(CoreService.class, service);
        assertTrue(AopUtils.isJdkDynamicProxy(service));
        Advised proxy = assertInstanceOf(Advised.class, service);
        Advisor advisor = transactionAdvisor(proxy);
        assertSame(context.getBean("coreTransactionAdvisor"), advisor);
        assertSame(context.getBean("transactionAdvice"), advisor.getAdvice());
        Advised userServiceProxy = assertInstanceOf(Advised.class, context.getBean("userService"));
        assertSame(transactionAdvisor(userServiceProxy).getAdvice(), advisor.getAdvice());
        assertThrows(BeanIsNotAFactoryException.class, () -> context.getBean("&coreService"));
        assertTrue(CoreService.class.isAssignableFrom(context.getType("coreService")));
        assertEquals(CoreServiceImpl.class, AopUtils.getTargetClass(service));
        assertFalse(context.containsBean("coreServiceTarget"));
    }

    @Test
    public void classOutsidePatternIsNotProxied() {
        // upgradeLevel 메소드가 있어도 클래스 이름이 *ServiceImpl이 아니면 프록시를 만들지 않는다.
        Object policy = context.getBean("userLevelUpgradePolicy");
        assertInstanceOf(DefaultUserLevelUpgradePolicy.class, policy);
        assertFalse(AopUtils.isAopProxy(policy));
    }

    private Advisor transactionAdvisor(Advised proxy) {
        // aop:config가 추가하는 기반 어드바이저와 구분해 트랜잭션 어드바이저가 한 번 적용됐는지 확인한다.
        List<Advisor> advisors = Arrays.stream(proxy.getAdvisors())
                .filter(advisor -> advisor.getAdvice() instanceof TransactionAdvice)
                .toList();
        assertEquals(1, advisors.size());
        return advisors.get(0);
    }

}
