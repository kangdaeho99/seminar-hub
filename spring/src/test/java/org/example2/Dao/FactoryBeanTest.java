package org.example2.Dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        assertInstanceOf(TransactionHandler.class, Proxy.getInvocationHandler(service));
        assertInstanceOf(TxProxyFactoryBean.class, context.getBean("&userService"));
        assertEquals(UserService.class, context.getType("userService"));
        assertEquals(UserServiceImpl.class, context.getBean("userServiceImpl").getClass());
    }

    @Test
    public void reuseTransactionProxyFactoryBeanForCoreService() {
        Object service = context.getBean("coreService");
        assertInstanceOf(CoreService.class, service);
        assertInstanceOf(TransactionHandler.class, Proxy.getInvocationHandler(service));
        assertInstanceOf(TxProxyFactoryBean.class, context.getBean("&coreService"));
        assertEquals(context.getBean("&userService").getClass(),
                context.getBean("&coreService").getClass());
        assertEquals(CoreService.class, context.getType("coreService"));
        assertEquals(CoreServiceImpl.class, context.getBean("coreServiceTarget").getClass());
    }


}
