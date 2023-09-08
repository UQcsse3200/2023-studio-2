package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the upgrade tree component for the game, which manages weapon upgrades.
 */
public class UpgradeTree extends Component {

    /** List of unlocked weapons for the player. */
    private final List<WeaponType> unlockedWeapons;

    /** The starting number of materials available to the player for upgrades. */
    private int materials = 0;

    /**
     * Constructs a new UpgradeTree with default weapons unlocked.
     */
    public UpgradeTree() {
        unlockedWeapons = new ArrayList<WeaponType>();

        // Base weapons
        unlockedWeapons.add(WeaponType.WOODHAMMER);
        unlockedWeapons.add(WeaponType.THROW_ELEC_WRENCH);
        unlockedWeapons.add(WeaponType.STICK);
    }

    /**
     * Returns a list of all unlocked weapons.
     * @return The list of unlocked weapons.
     */
    public List<WeaponType> getUnlockedWeapons() {
        return unlockedWeapons;
    }

    /**
     * Unlocks a specified weapon type, adding it to the list of unlocked weapons if it's not already there.
     * @param weapon The type of weapon to unlock.
     */
    public void unlockWeapon(WeaponType weapon) {
        if (!isWeaponUnlocked(weapon)) {
            unlockedWeapons.add(weapon);
        }
    }

    /**
     * Checks if a given weapon type is already unlocked.
     * @param weapon The type of weapon to check.
     * @return True if the weapon is unlocked, false otherwise.
     */
    public boolean isWeaponUnlocked(WeaponType weapon) {
        return unlockedWeapons.contains(weapon);
    }

    /**
     * Retrieves the current number of materials available to the player.
     * It fetches this value from the game's state observer, updating the local materials value.
     * @return The number of materials.
     */
    public int getMaterials() {
        GameStateObserver gameStateOb = ServiceLocator.getGameStateObserverService();
        String resourceKey = "resource/" + Resource.Nebulite;
        if (gameStateOb != null) {
            materials = (int) gameStateOb.getStateData(resourceKey);
        }
        return materials;
    }

    /**
     * Subtracts a given amount from the available materials.
     * The subtraction only happens if the materials available are more than or equal to the given amount.
     * @param amount The amount of materials to subtract.
     */
    public void subtractMaterials(int amount) {
        // todo: find a way to subtract from game state resources
        if (materials >= amount) {
            materials -= amount;
        }
    }
}
