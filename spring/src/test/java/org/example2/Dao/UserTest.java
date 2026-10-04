package org.example2.Dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        user.setLevel(Level.BASIC);

        user.upgradeLevel();
        assertEquals(Level.SILVER, user.getLevel());

        user.upgradeLevel();
        assertEquals(Level.GOLD, user.getLevel());
    }

    @Test
    public void cannotUpgradeLevel() {
        user.setLevel(Level.GOLD);

        IllegalStateException exception = assertThrows(IllegalStateException.class, user::upgradeLevel);

        assertEquals("GOLD 등급은 업그레이드가 불가능합니다.", exception.getMessage());
        assertEquals(Level.GOLD, user.getLevel());
    }
}
