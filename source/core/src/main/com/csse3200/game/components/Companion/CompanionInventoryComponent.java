package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
