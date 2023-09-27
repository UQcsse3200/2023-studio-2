package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * CompanionInventoryComponent handles all the inventory management for the companion.
 */
public class CompanionInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private static final int INVENTORY_SIZE = 7;

    // A single queue for all power-ups
    private final Deque<Entity> powerupQueue = new ArrayDeque<>(INVENTORY_SIZE);

    private final int[] itemQuantity = new int[INVENTORY_SIZE];
    private int equipped = 1;
//    private final HashMap<Integer, PotionType> equippedPMap = new HashMap<Integer, PotionType>();

//    @Override
//    public void create() {
//        equippedPMap.put(1, PotionType.DEATH_POTION);
//        equippedPMap.put(2, PotionType.SPEED_POTION);
//        equippedPMap.put(3, PotionType.HEALTH_POTION);
//        equippedPMap.put(4, PotionType.INVINCIBILITY_POTION);
//
//    }
//
//    public CompanionInventoryComponent() {
//        create();
//    }
//
//    public int getEquipped() {
//        return equipped;
//    }
//
//    public void setEquipped(int i) {
//        this.equipped = i;
//    }
//
//    public void replaceSlotWithPotion(int slot, PotionType potionType) {
//        if (slot > 3 || slot < 1) {
//            throw new IllegalArgumentException("Slot must be in range 1-3");
//        }
//        equippedPMap.remove(slot);
//        equippedPMap.put(slot, potionType);
//    }
//
//    public HashMap<Integer, PotionType> getEquippedPotionMap() {
//        return equippedPMap;
//    }
//
//    public void placeInSlot(PotionType potionType) {
//        int slot = switch (potionType) {
//            case DEATH_POTION -> 1;
//            case SPEED_POTION -> 2;
//            case HEALTH_POTION -> 3;
//            case INVINCIBILITY_POTION -> 4;
//            default -> throw new IllegalArgumentException("Slot not assigned: " + potionType);
//        };
//        replaceSlotWithPotion(slot, potionType);
//    }
//
//    public void cycleEquipped() {
//        int equiped = getEquipped();
//        if (equiped == 3) {
//            this.equipped = 1;
//        } else
//            this.equipped++;
//    }
//
//    public PotionType getEquippedType() {
//        return this.equippedPMap.get(getEquipped());
//    }
//
//    public void changeEquipped(PotionType potionType) {
//        System.out.println(potionType);
//        PotionType equippedType = getEquippedType();
//        this.equippedPMap.remove(equippedType);
//        this.equippedPMap.put(getEquipped(), potionType);
//    }
//


    /**
     * Adds a power-up item to the companion's inventory if there is space remaining.
     *
     * @param item An entity representing the power-up to be added.
     */
    public void addPowerup(Entity item) {
        if (powerupQueue.size() < INVENTORY_SIZE) {
            powerupQueue.add(item);
            ++itemQuantity[powerupQueue.size() - 1];
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
}
