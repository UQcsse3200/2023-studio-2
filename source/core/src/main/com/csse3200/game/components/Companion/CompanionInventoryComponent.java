/**
 * This class represents the inventory component for a companion character in the game.
 * It manages the companion's gold and provides methods for interacting with it.
 */
package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * CompanionInventoryComponent
 * Handles all the inventory management for the companion
 */
public class CompanionInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private static final  int INVENTORY_SIZE = 7;
    private List<Entity> inventory = new ArrayList<>(INVENTORY_SIZE);
    private int[] itemQuantity = new int[INVENTORY_SIZE];

    /**
     * AddItem adds an entity to the inventory if there is space remaining
     * @param item - an entity which can be put inside of the companions inventory
     */
    public void addItem(Entity item){
        if (inventory.size() < INVENTORY_SIZE) {
            inventory.add(item);
            ++itemQuantity[inventory.indexOf(item)];
        }
    }

    /**
     * I HAVE NO IDEA WHAT THIS FUNCTION DOES - MAXWELL
     * @param item - the item to check if it is in the inventory or not
     * @return - -1, if the item is not in the inventory. Otherwise, integer >=0 giving the index of that item in inventory
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
     * Return the item at a given index in the inventory
     * @param index - the index selecting the item in the inventory
     * @return - null if there is no item, or an entity if there is an item at that index.
     */
    public Entity getItem(Integer index) {
        //check the index is in valid bounds
        if ((index >= 0) && (index < INVENTORY_SIZE)) {
            if (inventory.get(index) != null) {
                return inventory.get(index);
            }
        }
        return null;
    }
}
