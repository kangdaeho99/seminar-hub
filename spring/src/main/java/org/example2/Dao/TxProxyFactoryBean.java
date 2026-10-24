package org.example2.Dao;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class TxProxyFactoryBean implements FactoryBean<Object> {

    Object target;
    PlatformTransactionManager transactionManager; // TransactionHandler를 생성할 떄 필요
    String pattern;
    Class<?> serviceInterface; // 다이내믹 프록시를 생성할떄 필요하다. UserService외의 인터페이스를 가진 타깃에도 적용할 수 있다.

    public void setTarget(Object target){
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    // FactoryBean 인터페이스 구현 메소드

    @Override
    public Object getObject() {
        TransactionHandler handler = new TransactionHandler();
        handler.setTarget(target);
        handler.setTransactionManager(transactionManager);
        handler.setPattern(pattern);
        return Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[] { serviceInterface }, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
