package org.example2.Service;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.example2.Dao.DefaultUserLevelUpgradePolicy;
import org.example2.Dao.Level;
import org.example2.Dao.User;
import org.example2.Dao.UserDao;
import org.example2.Dao.UserLevelUpgradePolicy;
import org.example2.Dao.UserService;
import org.example2.Dao.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_RECOCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"classpath:test-applicationContext.xml"}) 
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("testUserService")
    UserService testUserService;

    @Autowired
    MockMailSender mockMailSender;

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    DataSource dataSource;

    @Autowired
    ConfigurableApplicationContext context;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
            new User("bumjin", "박범진", "springno1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            new User("joytouch", "김영한", "springno2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, MIN_RECOCOMEND_FOR_GOLD + 10),
            new User("leegw700", "이길원", "springno3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 10, MIN_RECOCOMEND_FOR_GOLD - 1),
            new User("madnite1", "이상호", "springno4", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 10, MIN_RECOCOMEND_FOR_GOLD),
            new User("green", "유재성", "springno5", Level.GOLD, 100, 100)
        );
        for (User user : users) {
            user.setEmail(user.getId() + "@example.com");
        }
    }

    @Test 
    public void bean() {
        assertNotNull(this.userService);
        assertTrue(AopUtils.isJdkDynamicProxy(this.userService));
        assertTrue(AopUtils.isJdkDynamicProxy(this.testUserService));
        assertEquals(UserServiceImpl.class, AopUtils.getTargetClass(this.userService));
        assertEquals(TestUserServiceImpl.class, AopUtils.getTargetClass(this.testUserService));
        assertFalse(context.containsBean("userServiceImpl"));
        assertFalse(context.getBeanFactory().isFactoryBean("userService"));
        assertFalse(context.getBeanFactory().isFactoryBean("testUserService"));
        assertFalse(AopUtils.isAopProxy(this.userDao));
        assertFalse(AopUtils.isAopProxy(this.userLevelUpgradePolicy));
        assertNotNull(this.userLevelUpgradePolicy);
    }

    @Test
    public void transactionPointcut() throws NoSuchMethodException {
        PointcutAdvisor advisor = context.getBean("transactionAdvisor", PointcutAdvisor.class);
        Pointcut pointcut = advisor.getPointcut();

        // 내장/독립 포인트컷 모두 어드바이저를 통해 실제 적용 조건을 검증한다.
        for (Class<?> targetClass : Arrays.asList(UserServiceImpl.class, TestUserServiceImpl.class)) {
            assertTrue(pointcut.getClassFilter().matches(targetClass));
            assertTrue(pointcut.getMethodMatcher().matches(
                    UserService.class.getMethod("upgradeLevels"), targetClass));
            assertTrue(pointcut.getMethodMatcher().matches(
                    UserService.class.getMethod("add", User.class), targetClass));
        }
    }

    @Test
    public void transactionAttributes() throws NoSuchMethodException {
        TransactionInterceptor advice = context.getBean("transactionAdvice", TransactionInterceptor.class);
        assertSame(context.getBean("transactionManager"), advice.getTransactionManager());
        assertSame(advice, context.getBean("transactionAdvisor", PointcutAdvisor.class).getAdvice());
        TransactionAttributeSource source = advice.getTransactionAttributeSource();
        assertNotNull(source);

        TransactionAttribute upgrade = source.getTransactionAttribute(
                UserService.class.getMethod("upgradeLevels"), UserServiceImpl.class);
        assertNotNull(upgrade);
        assertEquals(TransactionDefinition.PROPAGATION_REQUIRES_NEW, upgrade.getPropagationBehavior());
        assertEquals(TransactionDefinition.ISOLATION_SERIALIZABLE, upgrade.getIsolationLevel());

        TransactionAttribute add = source.getTransactionAttribute(
                UserService.class.getMethod("add", User.class), UserServiceImpl.class);
        assertNotNull(add);
        assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, add.getPropagationBehavior());
        assertEquals(TransactionDefinition.ISOLATION_DEFAULT, add.getIsolationLevel());
        assertFalse(add.isReadOnly());
        assertTrue(add.rollbackOn(new RuntimeException()));
        assertTrue(add.rollbackOn(new AssertionError()));
        assertFalse(add.rollbackOn(new Exception()));

        // 아직 UserService에 get 메소드가 없으므로 기존 get 메소드로 이름 패턴만 검증한다.
        // DAO가 포인트컷 대상이라는 뜻은 아니다.
        TransactionAttribute get = source.getTransactionAttribute(
                UserDao.class.getMethod("get", String.class), null);
        assertNotNull(get);
        assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, get.getPropagationBehavior());
        assertTrue(get.isReadOnly());
        assertEquals(30, get.getTimeout());
    }

    @Test
    public void addParticipatesInExistingTransaction() {
        userDao.deleteAll();
        PlatformTransactionManager manager = context.getBean("transactionManager", PlatformTransactionManager.class);
        new TransactionTemplate(manager).executeWithoutResult(status -> {
            userService.add(users.get(0));
            assertEquals(1, userDao.getCount());
            status.setRollbackOnly();
        });
        assertEquals(0, userDao.getCount());
        checkTransactionReleased();
    }

    @Test
    public void upgradeCommitsIndependentlyOfExistingTransaction() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        PlatformTransactionManager manager = context.getBean("transactionManager", PlatformTransactionManager.class);
        new TransactionTemplate(manager).executeWithoutResult(status -> {
            userService.upgradeLevels();
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
            status.setRollbackOnly();
        });
        // upgrade*는 REQUIRES_NEW이므로 외부 트랜잭션이 롤백되어도 승급은 커밋된다.
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(3), true);
        checkTransactionReleased();
    }

    @Test
    public void add() {
        userDao.deleteAll();
        User withoutLevel = users.get(0);
        withoutLevel.setLevel(null);
        User withLevel = users.get(4);

        userService.add(withoutLevel);
        userService.add(withLevel);

        assertEquals(Level.BASIC, userDao.get(withoutLevel.getId()).getLevel());
        assertEquals(Level.GOLD, userDao.get(withLevel.getId()).getLevel());
        assertEquals(2, userDao.getCount());
        checkTransactionReleased();
    }

    @Test
    public void upgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // Mockito 대역으로 DB와 메일 서버 없이 등급 변경과 메일 요청을 검증한다.
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(users);
        userServiceImpl.setUserDao(mockUserDao);

        DefaultUserLevelUpgradePolicy policy = new DefaultUserLevelUpgradePolicy();
        policy.setUserDao(mockUserDao);
        userServiceImpl.setUserLevelUpgradePolicy(policy);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        // update()에 전달한 사용자와 호출 횟수를 검증한다.
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(mockUserDao).getAll();
        verify(mockUserDao, times(2)).update(userCaptor.capture());
        List<User> updated = userCaptor.getAllValues();
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

        // 실제 메일을 보내지 않고 send()에 전달한 메시지를 검증한다.
        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailCaptor.capture());
        List<SimpleMailMessage> messages = mailCaptor.getAllValues();
        assertArrayEquals(new String[]{"joytouch@example.com"}, messages.get(0).getTo());
        assertArrayEquals(new String[]{"madnite1@example.com"}, messages.get(1).getTo());
        verifyNoMoreInteractions(mockUserDao, mockMailSender);
    }

    @Test
    public void upgradeLevelsWithTransaction() {
        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
        assertEquals(users.size(), userDao.getCount());
        assertEquals(Arrays.asList("joytouch@example.com", "madnite1@example.com"),
                mockMailSender.getRequests());
        checkTransactionReleased();
    }

    @Test
    public void upgradeAllOrNothing() {
        // 예외 발생용 서비스도 빈으로 등록하여 자동 생성된 프록시를 통해 호출한다.
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        assertThrows(TestUserServiceException.class, testUserService::upgradeLevels);
        for (User user : users) {
            checkLevelUpgraded(user, false);
        }
        checkTransactionReleased();
    }

    private void checkTransactionReleased() {
        assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.hasResource(dataSource));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedLevel, updated.getLevel());
    }

    public static class TestUserServiceImpl extends UserServiceImpl {
        // 외부 테스트 인스턴스 없이 스프링이 생성할 수 있는 public static 내부 클래스다.
        // *ServiceImpl.*(..) 표현식으로 상속받은 upgradeLevels()에도 적용된다.
        private final String failOnUserId = "madnite1";

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(failOnUserId)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    private static class TestUserServiceException extends RuntimeException {
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User actual = userDao.get(user.getId());
        Level expectedLevel = upgraded ? user.getLevel().nextLevel() : user.getLevel();
        assertEquals(expectedLevel, actual.getLevel());
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getLogin(), actual.getLogin());
        assertEquals(user.getRecommend(), actual.getRecommend());
    }
    
}
