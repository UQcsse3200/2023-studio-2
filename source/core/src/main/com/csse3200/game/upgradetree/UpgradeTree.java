package com.csse3200.game.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class UpgradeTree {

    private final List<WeaponType> unlockedWeapons;
    private int materials = 100; // todo: increase brain cells because i cant find a way to get ship extractor resources

    public UpgradeTree() {
        unlockedWeapons = new ArrayList<WeaponType>();
    }

    public List<WeaponType> getUnlockedWeapons() {
        return unlockedWeapons;
    }

    public void unlockWeapon(WeaponType weapon) {
        if (!isWeaponUnlocked(weapon)) {
            unlockedWeapons.add(weapon);
        }
    }

    public boolean isWeaponUnlocked(WeaponType weapon) {
        return unlockedWeapons.contains(weapon);
    }

}
