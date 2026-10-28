package org.example2.config;

import java.util.List;

import org.example2.Dao.CoreDao;
import org.example2.Dao.CoreService;
import org.example2.Dao.Level;
import org.example2.Dao.User;
import org.example2.Dao.UserDao;
import org.example2.Dao.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PureSpringAopConfigTest {
    private AnnotationConfigApplicationContext context;
    private UserDao userDao;
    private UserService userService;
    private CoreDao coreDao;
    private CoreService coreService;
    private MailSender mailSender;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(configurationClass());
        userDao = context.getBean(UserDao.class);
        userService = context.getBean(UserService.class);
        coreDao = context.getBean(CoreDao.class);
        coreService = context.getBean(CoreService.class);
        mailSender = context.getBean(MailSender.class);
    }

    protected Class<?> configurationClass() {
        return TestConfig.class;
    }

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void createsJdkProxiesOnlyForMatchingBeans() {
        assertTrue(AopUtils.isJdkDynamicProxy(userService));
        assertTrue(AopUtils.isJdkDynamicProxy(coreService));
        assertFalse(AopUtils.isAopProxy(userDao));
        assertFalse(AopUtils.isAopProxy(coreDao));
    }

    @Test
    void upgradesUsersInsideTransactionAndCommits() {
        addUpgradeableUsers();
        doAnswer(invocation -> {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        userService.upgradeLevels();

        assertEquals(Level.SILVER, userDao.get("a-user").getLevel());
        assertEquals(Level.SILVER, userDao.get("b-user").getLevel());
        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }

    @Test
    void rollsBackAllUpgradesWhenSecondMailFails() {
        addUpgradeableUsers();
        doNothing().doThrow(new MailSendException("test failure"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(MailSendException.class, userService::upgradeLevels);

        assertEquals(Level.BASIC, userDao.get("a-user").getLevel());
        assertEquals(Level.BASIC, userDao.get("b-user").getLevel());
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }

    @Test
    void commitsCoreItems() {
        coreService.addAll(List.of("alpha", "beta"));

        assertEquals(List.of("alpha", "beta"), coreDao.getAll());
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }

    @Test
    void rollsBackCoreItemsOnDuplicate() {
        assertThrows(DuplicateKeyException.class,
                () -> coreService.addAll(List.of("alpha", "beta", "alpha")));

        assertEquals(List.of(), coreDao.getAll());
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }

    private void addUpgradeableUsers() {
        for (String id : List.of("a-user", "b-user")) {
            User user = new User(id, id, "password", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0);
            user.setEmail(id + "@example.com");
            userDao.add(user);
        }
    }

    @Configuration
    static class TestConfig extends PureSpringAopConfig {
        // 실제 Config의 서비스/AOP 구성을 사용하되 파일 DB와 SMTP 서버는 사용하지 않는다.
        @Override
        @Bean(destroyMethod = "shutdown")
        public EmbeddedDatabase dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(EmbeddedDatabaseType.H2)
                    .build();
        }

        @Override
        @Bean
        public MailSender mailSender() {
            return mock(MailSender.class);
        }
    }
}
