package org.example2.Dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
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
    public void getTransactionProxyFromFactoryBean() {
        Object service = context.getBean("userService");
        assertInstanceOf(UserService.class, service);
        assertTrue(AopUtils.isJdkDynamicProxy(service));
        Advised proxy = assertInstanceOf(Advised.class, service);
        assertSame(context.getBean("transactionAdvisor"), proxy.getAdvisors()[0]);
        assertInstanceOf(ProxyFactoryBean.class, context.getBean("&userService"));
        assertTrue(UserService.class.isAssignableFrom(context.getType("userService")));
        assertEquals(UserServiceImpl.class, context.getBean("userServiceImpl").getClass());
    }

    @Test
    public void reuseTransactionAdviceForCoreService() {
        Object service = context.getBean("coreService");
        assertInstanceOf(CoreService.class, service);
        assertTrue(AopUtils.isJdkDynamicProxy(service));
        Advised proxy = assertInstanceOf(Advised.class, service);
        assertSame(context.getBean("transactionAdvice"), proxy.getAdvisors()[0].getAdvice());
        Advised userServiceProxy = assertInstanceOf(Advised.class, context.getBean("userService"));
        assertSame(userServiceProxy.getAdvisors()[0].getAdvice(), proxy.getAdvisors()[0].getAdvice());
        assertInstanceOf(ProxyFactoryBean.class, context.getBean("&coreService"));
        assertEquals(context.getBean("&userService").getClass(),
                context.getBean("&coreService").getClass());
        assertTrue(CoreService.class.isAssignableFrom(context.getType("coreService")));
        assertEquals(CoreServiceImpl.class, context.getBean("coreServiceTarget").getClass());
    }


}
