package org.example2.Dao;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

// Spring이 생성된 다이내믹 프록시를 서비스 빈으로 제공하도록 한다.
public class TxProxyFactoryBean implements FactoryBean<Object> {
    private Object target; // TransactionHandler를 생성할 때 필요
    private PlatformTransactionManager transactionManager; // TransactionHandler를 생성할 때 필요
    private String pattern; // TransactionHandler를 생성할 때 필요
    private Class<?> serviceInterface; // UserService 외의 인터페이스를 가진 타깃에도 적용할 수 있다.

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() {
        TransactionHandler handler = new TransactionHandler();
        handler.setTarget(target);
        handler.setTransactionManager(transactionManager);
        handler.setPattern(pattern);
        return Proxy.newProxyInstance(
                serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
