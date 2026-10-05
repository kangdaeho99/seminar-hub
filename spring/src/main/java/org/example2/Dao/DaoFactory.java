package org.example2.Dao;

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
    public TransactionAdvice transactionAdvice() {
        TransactionAdvice advice = new TransactionAdvice();
        advice.setTransactionManager(transactionManager());
        return advice;
    }

    @Bean
    public AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..*ServiceImpl.upgrade*(..))");
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
