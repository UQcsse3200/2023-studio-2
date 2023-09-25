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
    private final List<Object> unlockedWeapons;

    /**
     * Constructs a new UpgradeTree with default weapons unlocked.
     */
    public UpgradeTree() {
        unlockedWeapons = new ArrayList<>();

        // Base weapons
        unlockedWeapons.add(WeaponType.MELEE_KATANA.toString());
        unlockedWeapons.add(WeaponType.RANGED_BOOMERANG.toString());
        unlockedWeapons.add(WeaponType.WOODHAMMER.toString());
    }

    /**
     * Returns a list of all unlocked weapons.
     *
     * @return The list of unlocked weapons.
     */
    public List<Object> getUnlockedWeapons() {
        return unlockedWeapons;
    }

    /**
     * Unlocks a specified weapon type, adding it to the list of unlocked weapons if it's not already there.
     * @param weapon The type of weapon to unlock.
     * unlockWeapon() - adding weapon on nodes if weapon is locked, also
     *              plays the sound when upgrading weapons
     */

    public void unlockWeapon(Object weapon) {
        if (!isWeaponUnlocked(weapon)) {
            unlockedWeapons.add(weapon);

            if (entity != null) {
                entity.getEvents().trigger("playSound", "upgradeWeapon");
            }

        }
    }

    /**
     * Checks if a given weapon type is already unlocked.
     * @param weapon The type of weapon to check.
     * @return True if the weapon is unlocked, false otherwise.
     */
    public boolean isWeaponUnlocked(Object weapon) {
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
            Object value = gameStateOb.getStateData(resourceKey);
            return value == null ? 0 : (int) value;
        }
        return 0;
    }

    /**
     * Subtracts a given amount from the available extractor resources.
     *
     * @param amount The amount of materials to subtract.
     */
    public void subtractMaterials(int amount) {
        if (getMaterials() >= amount) {
            ServiceLocator.getGameStateObserverService()
                    .trigger("resourceAdd", Resource.Nebulite.toString(), -amount);
//            entity.getEvents().trigger("playSound", "upgradeWeapon");
        }
    }
}
