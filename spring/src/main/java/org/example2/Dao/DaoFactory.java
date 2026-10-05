package org.example2.Dao;

import javax.sql.DataSource;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor;
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
    public AnnotationTransactionAttributeSource transactionAttributeSource() {
        // 6.7.1: 이름 패턴 대신 Spring의 @Transactional에서 트랜잭션 속성을 읽는다.
        return new AnnotationTransactionAttributeSource();
    }

    @Bean
    public TransactionInterceptor transactionAdvice() {
        TransactionInterceptor advice = new TransactionInterceptor();
        advice.setTransactionManager(transactionManager());
        advice.setTransactionAttributeSource(transactionAttributeSource());
        return advice;
    }

    @Bean
    public Pointcut transactionPointcut() {
        // Advisor 내부의 TransactionAttributeSourcePointcut을 사용한다.
        // 트랜잭션 속성이 있는 메소드를 선정하며, 빈을 새로 등록하는 역할은 아니다.
        return transactionAdvisor().getPointcut();
    }

    @Bean
    public TransactionAttributeSourceAdvisor transactionAdvisor() {
        // XML의 tx:annotation-driven이 자동 구성하는 역할을 Java 빈 설정으로 표현한다.
        return new TransactionAttributeSourceAdvisor(transactionAdvice());
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
