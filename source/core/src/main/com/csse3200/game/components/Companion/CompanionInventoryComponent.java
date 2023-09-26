/**
 * This class represents the inventory component for a companion character in the game.
 * It manages the companion's gold and provides methods for interacting with it.
 */
package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.PotionType;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * CompanionInventoryComponent handles all the inventory management for the companion.
 */
public class CompanionInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private static final int INVENTORY_SIZE = 7;
    private final List<Entity> inventory = new ArrayList<>(INVENTORY_SIZE);
    private final int[] itemQuantity = new int[INVENTORY_SIZE];
    private int equipped = 1;
    private final HashMap<Integer, PotionType> equippedPMap = new HashMap<Integer, PotionType>();

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
     * Adds an item to the companion's inventory if there is space remaining.
     *
     * @param item An entity which can be put inside the companion's inventory.
     */
    public void addItem(Entity item) {
        if (inventory.size() < INVENTORY_SIZE) {
            inventory.add(item);
            ++itemQuantity[inventory.indexOf(item)];
        }
    }

    /**
     * Retrieves the index of an item in the inventory.
     *
     * @param item The item to check if it is in the inventory or not.
     * @return -1 if the item is not in the inventory, otherwise an integer >= 0 indicating the index of that item in the inventory.
     */
    public int getItemIndex(Entity item) {
        int index = -1;
        for (int i = 0; i < inventory.size(); ++i) {
            if (inventory.get(i).equals(item)) {
                return i;
            }
        }
        return index;
    }

    /**
     * Returns the item at a given index in the inventory.
     *
     * @param index The index selecting the item in the inventory.
     * @return Null if there is no item, or an entity if there is an item at that index.
     */
    public Entity getItem(Integer index) {
        // Check if the index is within valid bounds
        if ((index >= 0) && (index < INVENTORY_SIZE)) {
            if (inventory.get(index) != null) {
                return inventory.get(index);
            }
        }
        return null;
    }
}
