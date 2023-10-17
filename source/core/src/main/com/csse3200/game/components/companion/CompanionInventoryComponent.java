package com.csse3200.game.components.companion;

import com.csse3200.game.components.companionweapons.CompanionWeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;




import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfigs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CompanionInventoryComponent handles all the inventory management for the companion.
 */
public class CompanionInventoryComponent extends Component {

    CompanionWeaponType weaponType;
    int ammoCount;
    int maxAmmo;
    private int attackCooldown;

    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private static final int INVENTORY_SIZE = 7;

    // A single queue for all power-ups
    private final Deque<Entity> powerupQueue = new ArrayDeque<>(INVENTORY_SIZE);


    // Add a HashMap to store counts for each power-up type. DOESN'T DO ANYTHING
    private final HashMap<PowerupType, Integer> powerupCounts = new HashMap<>();

    private  String equipped = "ranged";
    private final LinkedHashMap<String, CompanionInventoryComponent> equippedWMap = new LinkedHashMap<>(); // preserves insert order
    private CompanionWeaponConfigs config;

    public CompanionInventoryComponent(CompanionWeaponType weaponType, int ammo, int maxAmmo) {
        this.weaponType = weaponType;
        this.ammoCount = ammo;
        this.maxAmmo = maxAmmo;
        attackCooldown = 0;
    }

    public CompanionWeaponType getItem() {
        return this.weaponType;
    }

    public int getAmmo() {
        return this.ammoCount;
    }

    public void changeAmmo(int change) {
        ammoCount = Math.min(maxAmmo, Math.max(0, ammoCount + change));
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


    public CompanionInventoryComponent() {
        // Initialize power-up counts
        for (PowerupType type : PowerupType.values()) {
            powerupCounts.put(type, 0);
        }
    }

    private PowerupType identifyPowerupType(Entity item) {
        // Assuming PowerupComponent contains the type information
        PowerupComponent powerupComponent = item.getComponent(PowerupComponent.class);

        // Check if the PowerupComponent is present
        if (powerupComponent != null) {
            return powerupComponent.getType();
        } else {
            // Log a warning or handle the case where PowerupComponent is not present
            logger.warn("PowerupComponent not found in the item entity");
            return null;
        }
    }

    public int getPowerupCount(PowerupType type) {
        return powerupCounts.getOrDefault(type, 0);
    }

    public void create() {

        equippedWMap.put("ranged", new CompanionInventoryComponent(CompanionWeaponType.Death_Potion, 30, 30));
        equippedWMap.put("Fire", new CompanionInventoryComponent(CompanionWeaponType.SHIELD_2, 30, 30));
        equippedWMap.put("melee", new CompanionInventoryComponent(CompanionWeaponType.SWORD, 30, 30));

    }


    public void update() {
        this.equippedWMap.get(getEquipped()).decCoolDown();
    }

    public CompanionInventoryComponent(CompanionWeaponConfigs config) {
        create();
        this.config = config;
    }

    public  CompanionWeaponConfigs getConfigs() {
        return config;
    }

    /**
     * @return int - the equipped weapon
     */
    public  String getEquipped() {
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
     * Returns the equipped weapon type
     *
     * @return WeaponType - Type of cureently equiped weapon
     */
    public CompanionWeaponType getEquippedType() {
        return this.equippedWMap.get(getEquipped()).getItem();
    }

    public  int GetCurrentAmmo() {
        return this.equippedWMap.get(getEquipped()).getAmmo();
    }

    public void changeEquippedAmmo(int ammoChange) {this.equippedWMap.get(getEquipped()).changeAmmo(ammoChange);}

    public void setEquippedCooldown(int coolDown) {
        this.equippedWMap.get(getEquipped()).setAttackCooldown(coolDown);
    }
}
