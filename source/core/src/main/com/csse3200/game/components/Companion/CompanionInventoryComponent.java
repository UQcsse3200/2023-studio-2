package com.csse3200.game.components.Companion;

import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
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

    public void changeItem(CompanionWeaponType weaponType) {
        this.weaponType = weaponType;
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
    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private static final int INVENTORY_SIZE = 7;

    // A single queue for all power-ups
    private final Deque<Entity> powerupQueue = new ArrayDeque<>(INVENTORY_SIZE);

    private final int[] itemQuantity = new int[INVENTORY_SIZE];


    // Add a HashMap to store counts for each power-up type
    private final HashMap<PowerupType, Integer> powerupCounts = new HashMap<>();

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


    /**
     * Adds a power-up item to the companion's inventory if there is space remaining.
     *
     * @param item An entity representing the power-up to be added.
     */
    public void addPowerup(Entity item) {
        if (powerupQueue.size() < INVENTORY_SIZE) {
            powerupQueue.add(item);
            ++itemQuantity[powerupQueue.size() - 1];

            // Identify the power-up type and update the count
            PowerupType powerupType = identifyPowerupType(item);
            if (powerupType != null) {
                int currentCount = powerupCounts.get(powerupType);
                powerupCounts.put(powerupType, currentCount + 1);
                //System.out.println("Collecting powerup: " + powerupType + ", Count: " + powerupCounts.get(powerupType));
            }
            //System.out.println("Current powerupCounts: " + powerupCounts);
            //System.out.println("Collecting powerup: " + powerupType );

            logger.debug("item added");
        }
    }

    /**
     * Retrieves and uses the next power-up from the companion's inventory, regardless of its type.
     *
     * @return True if a power-up was successfully used, false if the inventory is empty.
     */
    public boolean useNextPowerup() {
        if (!powerupQueue.isEmpty()) {
            Entity powerup = powerupQueue.poll();
            powerup.getComponent(PowerupComponent.class).applyEffect();
            --itemQuantity[0];
            return true;
        }
        return false;
    }

    private  String equipped = "ranged";
    private final LinkedHashMap<String, CompanionInventoryComponent> equippedWMap = new LinkedHashMap<>(); // preserves insert order
    private CompanionWeaponConfigs config;


    public void create() {

        equippedWMap.put("ranged", new CompanionInventoryComponent(CompanionWeaponType.Death_Potion, 30, 30));
        equippedWMap.put("Fire", new CompanionInventoryComponent(CompanionWeaponType.SHIELD_2, 30, 30));

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
     * Replaces the specified slot with a given weapon.
     *
     * @param slot       the slot to be updated
     * @param weaponType the weapon type to be placed in the slot
     */
//    public void replaceSlotWithWeapon(String slot, CompanionWeaponType weaponType) {
//        equippedWMap.get(slot).changeItem(weaponType);
//    }

    /** Returns the current equipped weapons represented in a hash map **/
    public ArrayList<CompanionWeaponType> getEquippedWeapons() {
        return equippedWMap.values().stream().map(CompanionInventoryComponent::getItem).collect(Collectors.toCollection(ArrayList::new));
    }

    // not needed anymore?
//    public void placeInSlot(CompanionWeaponType weaponType) {
//        replaceSlotWithWeapon(config.GetWeaponConfig(weaponType).slotType, weaponType);
//    }

    /**
     * Updates weapon of the active inventory slot
     *
     */
    public void changeEquipped(CompanionWeaponType type) {
        this.equipped = config.GetWeaponConfig(type).slotType;
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

    public  int GetCurrentMaxAmmo() {
        return this.equippedWMap.get(getEquipped()).getMaxAmmo();
    }

    public void changeEquippedAmmo(int ammoChange) {this.equippedWMap.get(getEquipped()).changeAmmo(ammoChange);}

    public int getEquippedCooldown() {
        return this.equippedWMap.get(getEquipped()).getAttackCooldown();
    }

    public void setEquippedCooldown(int coolDown) {
        this.equippedWMap.get(getEquipped()).setAttackCooldown(coolDown);
    }
}
