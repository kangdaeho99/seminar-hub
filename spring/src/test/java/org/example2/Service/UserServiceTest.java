package org.example2.Service;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import org.example2.Dao.DefaultUserLevelUpgradePolicy;
import org.example2.Dao.Level;
import org.example2.Dao.User;
import org.example2.Dao.UserDao;
import org.example2.Dao.UserService;
import org.example2.Dao.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_RECOCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"classpath:test-applicationContext.xml"}) 
public class UserServiceTest {

    // 같은 타입의 빈은 필드 이름과 일치하는 빈 이름으로 구분해서 주입받는다.
    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    @Autowired 
    PlatformTransactionManager transactionManager;

    @Autowired
    MockMailSender mockMailSender;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    public void setUp() {
        // 컨텍스트는 재사용하되 테스트마다 메일 전송 기록은 초기화한다.
        mockMailSender.getRequests().clear();
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
    }

    @Test
    public void advisorAutoProxyCreator() {
        // JDK 동적 프록시는 java.lang.reflect.Proxy를 상속한다.
        assertInstanceOf(Proxy.class, testUserService);
    }

    @Test
    public void injectedServicesAreAutomaticallyProxied() {
        // 구현체를 직접 등록해도 @Autowired로 주입되는 것은 자동 생성된 프록시다.
        assertTrue(AopUtils.isJdkDynamicProxy(userService));
        assertEquals(UserServiceImpl.class, AopUtils.getTargetClass(userService));
        assertTrue(AopUtils.isJdkDynamicProxy(testUserService));
        assertEquals(TestUserServiceImpl.class, AopUtils.getTargetClass(testUserService));
    }

    @Test
    public void upgradeAllOrNothing() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        // 컨테이너가 테스트용 서비스에도 자동으로 적용한 트랜잭션을 통해 롤백을 검증한다.
        assertThrows(TestUserServiceException.class, testUserService::upgradeLevels);
        for (User user : users) {
            checkLevelUpgraded(user, false);
        }
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedLevel, updated.getLevel());
    }

    public static class TestUserServiceImpl extends UserServiceImpl {
        // 외부 테스트 인스턴스 없이 스프링이 생성할 수 있는 public static 내부 클래스다.
        // *ServiceImpl 클래스 필터를 통과하고, upgrade* 메소드에 트랜잭션이 적용된다.
        // 네 번째 사용자의 승급에서 실패시켜 앞선 사용자의 변경도 롤백되는지 확인한다.
        private final String failOnUserId = "madnite1";

        @Override
        public void upgradeLevels() {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
            super.upgradeLevels();
        }

        @Override
        public List<User> getAll() {
            assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
            return super.getAll();
        }

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

    @Test
    public void methodOutsidePatternDoesNotStartTransaction() {
        userDao.deleteAll();
        userDao.add(users.get(0));
        // getAll은 upgrade* 패턴에 해당하지 않아 트랜잭션을 시작하지 않는다.
        List<User> result = testUserService.getAll();
        assertEquals(1, result.size());
        assertEquals(users.get(0).getId(), result.get(0).getId());
    }

    @Test
    public void methodOutsidePatternPreservesTargetException() {
        userDao.deleteAll();
        assertThrows(EmptyResultDataAccessException.class,
                () -> userService.get("missing-user"));
    }
    
    @Test 
    public void transactionSync() {
        int initialCount = userDao.getCount();
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition); 

        try {
            userService.deleteAll();
            userService.add(users.get(0));
            userService.add(users.get(1));
            assertEquals(2, userDao.getCount());
        } finally {
            transactionManager.rollback(txStatus); 
        }
        assertEquals(initialCount, userDao.getCount());
    }
}
