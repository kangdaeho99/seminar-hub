package org.example2.Dao;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

// XML의 ProxyFactoryBean들이 공유하는 트랜잭션 어드바이스다.
public class TransactionAdvice implements MethodInterceptor {

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    // 타깃을 호출하는 기능을 가진 콜백 오브젝트를 프록시로부터 받는다.
    // 덕분에 어드바이스는 특정 타깃에 의존하지 않고 재사용 가능하다.
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = invocation.proceed(); // 다음 어드바이스를 거쳐 타깃 메소드를 호출한다.
            this.transactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) {
            // 스프링이 InvocationTargetException의 포장을 풀어 원래 타깃 예외를 전달한다.
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
