package org.example2.Dao;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

// applicationContext.xml과 같은 자동 프록시 구성을 Java 설정으로 표현한다.
@Configuration
public class DaoFactory {
    
    @Bean(initMethod = "initialize")
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        // 빈 후처리기를 먼저 등록할 수 있도록 static 팩토리 메소드로 선언한다.
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public TransactionInterceptor transactionAdvice() {
        // 토비의 스프링 6.6.2: 직접 만든 TransactionAdvice 대신 표준 인터셉터를 사용한다.
        TransactionInterceptor advice = new TransactionInterceptor();
        advice.setTransactionManager(transactionManager());

        // 6.6.4: XML의 tx:attributes와 같은 설정이다. 서비스의 get*는 읽기 전용,
        // 나머지 메소드는 읽기/쓰기로 실행하고 모두 REQUIRED를 사용한다.
        // REQUIRED: 기존 트랜잭션에 참여하며, 없으면 새로 시작한다.
        // REQUIRES_NEW: 기존 트랜잭션을 보류하고 독립적인 트랜잭션을 시작한다.
        // NOT_SUPPORTED: 기존 트랜잭션을 보류하고 트랜잭션 없이 실행한다.
        // 필요하면 해당 메소드 패턴의 값에 PROPAGATION_NOT_SUPPORTED를 지정한다.
        Properties attributes = new Properties();
        attributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly");
        attributes.setProperty("*", "PROPAGATION_REQUIRED");
        advice.setTransactionAttributes(attributes);

        // 6.6.2의 timeout_30, PROPAGATION_REQUIRES_NEW, ISOLATION_SERIALIZABLE은
        // 속성 지정 예제다. 별도 업무 요구가 없는 현재 공통 전략에서는 지정하지 않는다.
        // 격리 수준을 생략하면 DB 기본값(DEFAULT)을 사용하며, 기존 트랜잭션 참여 시에는
        // 기존 격리 수준을 따른다. 기본 롤백 대상은 RuntimeException과 Error다.
        return advice;
    }

    @Bean
    public AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 타입 패턴 execution(* *..*ServiceImpl.*(..)) 대신 서비스 빈 이름 규칙을 사용한다.
        // bean()은 Spring의 빈 생성 과정에서 이름을 기준으로 매칭하는 포인트컷이다.
        pointcut.setExpression("bean(*Service)");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    }

    @Bean
    public UserService userService() {
        // 타깃을 등록하면 후처리기가 UserService 인터페이스의 JDK 프록시로 감싼다.
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
        userService.setMailSender(mailSender());
        return userService;
    }

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(25);
        mailSender.setDefaultEncoding("UTF-8");
        return mailSender;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        DefaultUserLevelUpgradePolicy policy = new DefaultUserLevelUpgradePolicy();
        policy.setUserDao(userDao());
        return policy;
    }

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:./springbook");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    
}
