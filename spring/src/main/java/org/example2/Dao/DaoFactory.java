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

        // 메소드 이름 패턴별로 전파, 격리 수준, 읽기 전용, 제한시간을 지정한다.
        // REQUIRED: 기존 트랜잭션에 참여하며, 없으면 새로 시작한다.
        // REQUIRES_NEW: 기존 트랜잭션을 보류하고 독립적인 트랜잭션을 시작한다.
        // NOT_SUPPORTED: 기존 트랜잭션을 보류하고 트랜잭션 없이 실행한다.
        // 필요하면 해당 메소드 패턴의 값에 PROPAGATION_NOT_SUPPORTED를 지정한다.
        Properties attributes = new Properties();
        attributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly,timeout_30");
        attributes.setProperty("upgrade*", "PROPAGATION_REQUIRES_NEW,ISOLATION_SERIALIZABLE");
        attributes.setProperty("*", "PROPAGATION_REQUIRED");
        advice.setTransactionAttributes(attributes);

        // 격리 수준을 생략하면 DB 기본값(DEFAULT)을 사용하며, 기존 트랜잭션 참여 시에는
        // 기존 격리 수준을 따른다. 기본 롤백 대상은 RuntimeException과 Error다.
        return advice;
    }

    @Bean
    public AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..*ServiceImpl.*(..))");
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
