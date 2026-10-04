package org.example2.Service;

import java.util.Arrays;
import java.util.List;

import org.example2.Dao.Level;
import org.example2.Dao.User;
import org.example2.Dao.UserDao;
import org.example2.Dao.UserLevelUpgradePolicy;
import org.example2.Dao.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static org.example2.Dao.DefaultUserLevelUpgradePolicy.MIN_RECOCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"classpath:test-applicationContext.xml"}) 
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

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
        userDao.deleteAll();
    }

    @Test 
    public void bean() {
        assertNotNull(this.userService);
        assertNotNull(this.userLevelUpgradePolicy);
    }

    @Test
    public void upgradeLevels() {
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
