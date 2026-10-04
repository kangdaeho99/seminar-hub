package org.example2.Service;

import java.util.ArrayList;
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
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_RECOCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    DataSource dataSource;

    @Autowired
    ApplicationContext context;

    DummyMailSender dummyMailSender;

    List<User> users;

    @BeforeEach
    public void setUp() {
        dummyMailSender = new DummyMailSender();
        userServiceImpl.setMailSender(dummyMailSender);

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
        assertNotNull(this.userServiceImpl);
        assertNotNull(this.userLevelUpgradePolicy);
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
        MockUserDao mockUserDao = new MockUserDao(users);
        userServiceImpl.setUserDao(mockUserDao);

        // 별도로 분리한 등급 정책도 같은 Mock DAO를 사용해야 DB에 접근하지 않는다.
        DefaultUserLevelUpgradePolicy policy = new DefaultUserLevelUpgradePolicy();
        policy.setUserDao(mockUserDao);
        userServiceImpl.setUserLevelUpgradePolicy(policy);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertEquals(2, updated.size());
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
        assertEquals(Arrays.asList("joytouch@example.com", "madnite1@example.com"),
                mockMailSender.getRequests());
    }

    @Test
    public void MockUpgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // 직접 작성한 MockUserDao 대신 Mockito가 만든 대역을 사용한다.
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
        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

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
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setUserLevelUpgradePolicy(userLevelUpgradePolicy);
        testUserService.setMailSender(dummyMailSender);

        // &를 붙이면 생성된 프록시 대신 XML에 등록한 팩토리 빈 자체를 가져온다.
        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        assertThrows(TestUserServiceException.class, txUserService::upgradeLevels);
        checkLevelUpgraded(users.get(1), false);
    }

    private void checkTransactionReleased() {
        assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.hasResource(dataSource));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedLevel, updated.getLevel());
    }

    private static class MockUserDao implements UserDao {
        private final List<User> users;
        private final List<User> updated = new ArrayList<>();

        MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestUserService extends UserServiceImpl {
        private final String failOnUserId;

        TestUserService(String failOnUserId) {
            this.failOnUserId = failOnUserId;
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
    
}
