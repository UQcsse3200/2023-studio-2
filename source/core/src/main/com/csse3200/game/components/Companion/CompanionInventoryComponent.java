/**
 * This class represents the inventory component for a companion character in the game.
 * It manages the companion's gold and provides methods for interacting with it.
 */
package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanionInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionInventoryComponent.class);
    private int gold;

    /**
     * Constructs a new CompanionInventoryComponent with the specified initial gold amount.
     *
     * @param gold The initial amount of gold for the companion.
     */
    public CompanionInventoryComponent(int gold) {
        setGold(gold);
    }

    /**
     * Gets the current amount of gold the companion has.
     *
     * @return The current gold amount.
     */
    public int getGold() {
        return this.gold;
    }

    /**
     * Checks if the companion has at least the specified amount of gold.
     *
     * @param gold The amount of gold to check for.
     * @return True if the companion has at least the specified gold amount, false otherwise.
     */
    public Boolean hasGold(int gold) {
        return this.gold >= gold;
    }

    /**
     * Sets the companion's gold to the specified amount.
     *
     * @param gold The new gold amount.
     */
    public void setGold(int gold) {
        this.gold = Math.max(gold, 0);
        logger.debug("Setting gold to {}", this.gold);
    }

    /**
     * Adds the specified amount of gold to the companion's current gold.
     *
     * @param gold The amount of gold to add.
     */
    public void addGold(int gold) {
        setGold(this.gold + gold);
    }
}
