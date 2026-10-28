package org.example2.config;

import javax.sql.DataSource;

import org.example2.Dao.CoreDao;
import org.example2.Dao.CoreDaoJdbc;
import org.example2.Dao.CoreService;
import org.example2.Dao.CoreServiceImpl;
import org.example2.Dao.DefaultUserLevelUpgradePolicy;
import org.example2.Dao.NameMatchClassMethodPointcut;
import org.example2.Dao.TransactionAdvice;
import org.example2.Dao.UserDao;
import org.example2.Dao.UserDaoJdbc;
import org.example2.Dao.UserLevelUpgradePolicy;
import org.example2.Dao.UserService;
import org.example2.Dao.UserServiceImpl;
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
 * Spring의 클래스/메소드 이름 매칭으로 트랜잭션을 적용하는 Java Config 예제.
 * 기존 applicationContext.xml 또는 DaoFactory 대신 이 설정을 단독으로 등록한다.
 * 사용 예: new AnnotationConfigApplicationContext(PureSpringAopConfig.class)
 */
@Configuration
public class PureSpringAopConfig {

    // 1. 자동 프록시 생성기: 등록된 Advisor를 찾아 적용 대상 빈을 프록시로 감싼다.
    @Bean
    public static DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        // 빈 후처리기를 일찍 등록하면서 설정 클래스가 조기 생성되지 않도록 static으로 선언한다.
        return new DefaultAdvisorAutoProxyCreator();
    }

    // 2. 어드바이스: 기존에 구현한 트랜잭션 시작/커밋/롤백 코드를 재사용한다.
    @Bean
    public TransactionAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        TransactionAdvice advice = new TransactionAdvice();
        advice.setTransactionManager(transactionManager);
        return advice;
    }

    // 3. 포인트컷: 클래스 이름과 메소드 이름의 패턴으로 적용 대상을 선정한다.
    @Bean
    public NameMatchClassMethodPointcut transactionPointcut() {
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    // 4. 어드바이저: 어디에 적용할지(Pointcut)와 무엇을 실행할지(Advice)를 연결한다.
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(
            @Qualifier("transactionPointcut") NameMatchClassMethodPointcut pointcut,
            TransactionAdvice transactionAdvice) {
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice);
    }

    // 기존 XML과 동일하게 CoreServiceImpl의 모든 메소드에도 같은 어드바이스를 적용한다.
    @Bean
    public NameMatchClassMethodPointcut coreTransactionPointcut() {
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("CoreServiceImpl");
        pointcut.setMappedName("*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor coreTransactionAdvisor(
            @Qualifier("coreTransactionPointcut") NameMatchClassMethodPointcut pointcut,
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
}
