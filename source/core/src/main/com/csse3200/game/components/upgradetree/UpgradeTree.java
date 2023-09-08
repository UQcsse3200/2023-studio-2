package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class UpgradeTree extends Component {

    private final List<WeaponType> unlockedWeapons;
    private int materials = 100;

    public UpgradeTree() {
        unlockedWeapons = new ArrayList<WeaponType>();

        // Base weapons
        unlockedWeapons.add(WeaponType.WOODHAMMER);
        unlockedWeapons.add(WeaponType.ELEC_WRENCH);
        unlockedWeapons.add(WeaponType.STICK);
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

    public int getMaterials() {
        GameStateObserver gameStateOb = ServiceLocator.getGameStateObserverService();
        String resourceKey = "resource/" + Resource.Nebulite;
        if (gameStateOb != null) {
            materials = (int) gameStateOb.getStateData(resourceKey);
        }
        return materials;
    }

    public void subtractMaterials(int amount) {
        if (materials >= amount) {
            materials -= amount;
        }
    }
}
