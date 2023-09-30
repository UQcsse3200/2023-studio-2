package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PowerupConfig;
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



}
