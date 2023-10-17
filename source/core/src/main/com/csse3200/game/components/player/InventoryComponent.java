package com.csse3200.game.components.player;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A component intended to be used by the player to track their inventory.
 * Player can switch between weapons and weapons can be updated
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
    private String equipped = "melee";
    private LinkedHashMap<String, WeaponType> slotTypeMap = new LinkedHashMap<>();
    private LinkedHashMap<String, InventoryItem> equippedWMap = new LinkedHashMap<>(); // preserves insert order
    private final WeaponConfigs config;
    private boolean reload;

    public InventoryComponent(WeaponConfigs config, PlayerConfig playerConfig) {
        create(playerConfig);
        this.config = config;
    }

    public void create(PlayerConfig playerConfig) {
        this.slotTypeMap = playerConfig.slotTypeMap;
        this.equipped = playerConfig.equipped;
        equippedWMap = convertSave(slotTypeMap);
    }

    @Override
    public void update() {
        this.equippedWMap.get(getEquipped()).decCoolDown();
    }

    /**
    * Converts the saved map into a map containing the item's slot, and assigned ammo.
     *
     * This was done as SavableComponent cannot take the non-static `InventoryItem` class
    */
    public LinkedHashMap<String, InventoryItem> convertSave(LinkedHashMap<String, WeaponType> input) {
        LinkedHashMap<String, InventoryItem> temp = new LinkedHashMap<>();
        if (input == null) {
            return temp;
        }
        for (var entry : input.entrySet()) {
            if (entry.getKey().equals("melee")) {
                temp.put(entry.getKey(), new InventoryItem(entry.getValue(), 30, 30));
            } else if (entry.getKey().equals("ranged")) {
                temp.put(entry.getKey(), new InventoryItem(entry.getValue(), 10, 10));
            } else {
                temp.put(entry.getKey(), new InventoryItem(entry.getValue()));
            }
        }
        return temp;
    }

    /**
     * Returns a map containing the weapon's slot, and its type
     * @return LinkedHashMap containing a slot's String and its WeaponType
     */
    public LinkedHashMap<String, WeaponType> getSlotTypeMap() {
        return slotTypeMap;
    }

    /**
     * Gets the weapon configs
     * @return WeaponConfigs - all weapon configs
     */
    public WeaponConfigs getConfigs() {
        return config;
    }

    /**
     * Gets the equipped weapon's assigned slot
     * @return String - the equipped weapon's slot: melee, ranged, or building
     */
    public String getEquipped() {
        return equipped;
    }

    /**
     * Changes active inventory slot to a specific slot
     *
     * @param slot - the weapon to be equipped
     */
    public void setEquipped(String slot) {
        this.equipped = slot;
    }

    /**
     * Replaces the specified slot with a given weapon.
     *
     * @param slot       the slot to be updated
     * @param weaponType the weapon type to be placed in the slot
     */
    public void replaceSlotWithWeapon(String slot, WeaponType weaponType) {
        equippedWMap.get(slot).changeItem(weaponType);
        slotTypeMap.put(slot, weaponType);
        if (Objects.equals(slot, equipped)) {
            entity.getEvents().trigger("changeWeapon", weaponType);
        }
    }

    /**
     * Returns the current equipped weapons represented in an array
     **/
    public ArrayList<WeaponType> getEquippedWeapons() {
        return equippedWMap.values().stream().map(InventoryItem::getItem)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a mapping of item and its respective slot
     * 
     * @return LinkedHashMap - a hash map containing the slot and item
     */
    public LinkedHashMap<String, InventoryItem> getEquippedWMap() {
        return equippedWMap;
    }

    /**
     * Updates weapon of the active inventory slot
     */
    public void changeEquipped(WeaponType type) {
        this.equipped = config.GetWeaponConfig(type).slotType;
    }

    /**
     * Returns the equipped weapon type
     *
     * @return WeaponType - Type of cureently equiped weapon
     */
    public WeaponType getEquippedType() {
        return this.equippedWMap.get(getEquipped()).getItem();
    }

    /**
     * Get the currently equipped weapons current ammo
     *
     * @return remaining ammo
     */
    public int getCurrentAmmo() {
        return this.equippedWMap.get(getEquipped()).getAmmo();
    }

    /**
     * Changes the amount of ammo in current weapon slot by change (decrease for
     * negative values of ammoChange)
     *
     * @param ammoChange - amount to change ammo by
     */
    public void changeEquippedAmmo(int ammoChange) {
        this.equippedWMap.get(getEquipped()).changeAmmo(ammoChange);
    }

    /**
     * returns the amount of ammo the currently equipped weapon uses per shot
     *
     * @return amount of ammo use per shot
     */
    public int getCurrentAmmoUse() {
        return config.GetWeaponConfig(getEquippedType()).ammoUse;
    }

    /**
     * REturns the amount of max ammo for the current weapon slot
     *
     * @return
     */
    public int getCurrentMaxAmmo() {
        return this.equippedWMap.get(getEquipped()).getMaxAmmo();
    }

    /**
     * returns the remaining tick cooldown for the currently equipped weapon slot
     *
     * @return amount of ticks before weapon slot can be used again
     */
    public int getEquippedCooldown() {
        return this.equippedWMap.get(getEquipped()).getAttackCooldown();
    }

    /**
     * Sets the cooldown for the currently equipped weapon slot
     *
     * @param coolDown - tick duration to set coolDown to
     */
    public void setEquippedCooldown(int coolDown) {
        this.equippedWMap.get(getEquipped()).setAttackCooldown(coolDown);
    }

    /**
     * Function to reload weapon (after 2second delay)
     * cant reload if weapon is under cooldown
     */
    public void reloadWeapon() {
        if (getEquippedCooldown() > 0) {
            return;
        }
        reload = true;
        final Timer placeTimer = new Timer();
        Timer.Task placeEntity = new Timer.Task() {
            @Override
            public void run() {
                equippedWMap.get(getEquipped()).setMaxAmmo();
                reload = false;
                entity.getEvents().trigger("updateAmmo", getCurrentAmmo(),
                        getCurrentMaxAmmo(), getCurrentAmmoUse());
            }
        };
        entity.getEvents().trigger("updateAmmo", getCurrentAmmo(),
                getCurrentMaxAmmo(), getCurrentAmmoUse());
        placeTimer.scheduleTask(placeEntity, 2);
    }

    /**
     * Return if the weapon is being reloaded
     * 
     * @return True if the current equipped weapon is being reloaded
     */
    public boolean getReloading() {
        return reload;
    }

    /**
     * Private class to store inventory items
     */
    public class InventoryItem {
        private WeaponType weaponType;
        private int ammoCount;
        private int maxAmmo;
        private int attackCooldown = 0;

        public InventoryItem(WeaponType weaponType, int ammo, int maxAmmo) {
            this.weaponType = weaponType;
            this.ammoCount = ammo;
            this.maxAmmo = maxAmmo;
        }

        public InventoryItem(WeaponType weaponType) {
            this.weaponType = weaponType;
            ammoCount = 100;
            maxAmmo = 100;
        }

        public WeaponType getItem() {
            return this.weaponType;
        }

        public void changeItem(WeaponType weaponType) {
            this.weaponType = weaponType;
        }

        public int getAmmo() {
            return this.ammoCount;
        }

        public void changeAmmo(int change) {
            ammoCount = Math.min(maxAmmo, Math.max(0, ammoCount + change));
        }

        public void setMaxAmmo() {
            ammoCount = maxAmmo;
        }

        public int getMaxAmmo() {
            return this.maxAmmo;
        }

        public void setAttackCooldown(int cooldown) {
            this.attackCooldown = cooldown;
        }

        public int getAttackCooldown() {
            return this.attackCooldown;
        }

        public void decCoolDown() {
            if (attackCooldown == 0)
                return;
            attackCooldown--;
        }
    }
}