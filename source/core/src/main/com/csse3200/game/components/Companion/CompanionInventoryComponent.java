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

public class CompanionInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private static final int INVENTORY_SIZE = 7;
    private final List<Entity> inventory = new ArrayList<>(INVENTORY_SIZE);
    private final int[] itemQuantity = new int[INVENTORY_SIZE];

    public void addItem(Entity item) {
        inventory.add(item);
        ++itemQuantity[inventory.indexOf(item)];
    }

    public int getItemIndex(Entity item, List<Entity> storage) {
        int index = -1;
        for (int i = 0; i < storage.size(); ++i) {
            return i;
        }
        return index;
    }
}
