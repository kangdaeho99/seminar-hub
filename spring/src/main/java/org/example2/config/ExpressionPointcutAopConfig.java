package org.example2.config;

import javax.sql.DataSource;

import org.example2.Dao.CoreDao;
import org.example2.Dao.CoreDaoJdbc;
import org.example2.Dao.CoreService;
import org.example2.Dao.CoreServiceImpl;
import org.example2.Dao.DefaultUserLevelUpgradePolicy;
import org.example2.Dao.MessageFactoryBean;
import org.example2.Dao.TransactionAdvice;
import org.example2.Dao.UserDao;
import org.example2.Dao.UserDaoJdbc;
import org.example2.Dao.UserLevelUpgradePolicy;
import org.example2.Dao.UserService;
import org.example2.Dao.UserServiceImpl;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * applicationContext.xml의 포인트컷 표현식과 서비스 구성을 옮긴 Java Config 예제.
 * AspectJ는 표현식 해석/매칭에 사용하고, 프록시와 트랜잭션 처리는 Spring이 담당한다.
 * XML, DaoFactory, PureSpringAopConfig와 함께 등록하지 않고 이 설정을 단독으로 사용한다.
 * 사용 예: new AnnotationConfigApplicationContext(ExpressionPointcutAopConfig.class)
 */
@Configuration
public class ExpressionPointcutAopConfig {

    // 1. 자동 프록시 생성기: Advisor의 포인트컷에 맞는 빈에 Spring 프록시를 생성한다.
    @Bean
    public static DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        // 빈 후처리기를 등록할 때 설정 클래스가 조기 생성되지 않도록 static으로 선언한다.
        return new DefaultAdvisorAutoProxyCreator();
    }

    // 2. 어드바이스: 기존에 구현한 트랜잭션 시작/커밋/롤백 코드를 재사용한다.
    @Bean
    public TransactionAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        TransactionAdvice advice = new TransactionAdvice();
        advice.setTransactionManager(transactionManager);
        return advice;
    }

    // 3. XML의 <aop:pointcut>: AspectJ 표현식을 해석하는 포인트컷을 직접 등록한다.
    @Bean
    public AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..*ServiceImpl.upgrade*(..))");
        return pointcut;
    }

    // 4. XML의 <aop:advisor>: 포인트컷과 어드바이스를 연결한다.
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(
            @Qualifier("transactionPointcut") AspectJExpressionPointcut pointcut,
            TransactionAdvice transactionAdvice) {
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice);
    }

    @Bean
    public AspectJExpressionPointcut coreTransactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..CoreServiceImpl.*(..))");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor coreTransactionAdvisor(
            @Qualifier("coreTransactionPointcut") AspectJExpressionPointcut pointcut,
            TransactionAdvice transactionAdvice) {
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice);
    }

    // 아래는 이 Config만으로 실행할 수 있도록 연결한 서비스와 기반 빈들이다.
    @Bean
    public UserService userService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy,
            MailSender mailSender) {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserDao(userDao);
        service.setUserLevelUpgradePolicy(userLevelUpgradePolicy);
        service.setMailSender(mailSender);
        service.setMailFrom("noreply@example.com");
        return service;
    }

    @Bean
    public CoreService coreService(CoreDao coreDao) {
        CoreServiceImpl service = new CoreServiceImpl();
        service.setCoreDao(coreDao);
        return service;
    }

    @Bean(initMethod = "initialize")
    public UserDaoJdbc userDao(DataSource dataSource) {
        UserDaoJdbc dao = new UserDaoJdbc();
        dao.setDataSource(dataSource);
        return dao;
    }

    @Bean(initMethod = "initialize")
    public CoreDaoJdbc coreDao(DataSource dataSource) {
        CoreDaoJdbc dao = new CoreDaoJdbc();
        dao.setDataSource(dataSource);
        return dao;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(UserDao userDao) {
        DefaultUserLevelUpgradePolicy policy = new DefaultUserLevelUpgradePolicy();
        policy.setUserDao(userDao);
        return policy;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:./springbook");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
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
    public MessageFactoryBean message() {
        MessageFactoryBean factory = new MessageFactoryBean();
        factory.setText("Factory Bean");
        return factory;
    }
}
