package org.example2.Dao;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);
}
