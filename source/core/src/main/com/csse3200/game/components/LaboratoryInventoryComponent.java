/**
 * Component representing the inventory of a laboratory in the game.
 * It manages the number of potions in the laboratory and provides methods
 * for accessing and modifying the potion count.
 */
package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LaboratoryInventoryComponent handles the inventory of a laboratory,
 * specifically the number of potions it contains.
 */
public class LaboratoryInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(LaboratoryInventoryComponent.class);
    private int potion;

    /**
     * Initializes a LaboratoryInventoryComponent with the specified initial number of potions.
     *
     * @param potion The initial number of potions in the laboratory.
     */
    public LaboratoryInventoryComponent(int potion) {
        setPotion(potion);
    }

    /**
     * Gets the current number of potions in the laboratory.
     *
     * @return The current number of potions.
     */
    public int getPotion() {
        return this.potion;
    }

    /**
     * Checks if the laboratory has at least a certain number of potions.
     *
     * @param potion The number of potions to check.
     * @return True if the laboratory has at least the specified number of potions, false otherwise.
     */
    public Boolean hasPotion(int potion) {
        return this.potion >= potion;
    }

    /**
     * Sets the number of potions in the laboratory.
     *
     * @param potion The new number of potions.
     */
    public void setPotion(int potion) {
        this.potion = Math.max(potion, 0);
        logger.debug("Setting potion to {}", this.potion);
    }

    /**
     * Adds a certain number of potions to the laboratory's inventory.
     *
     * @param potion The number of potions to add.
     */
    public void addPotion(int potion) {
        setPotion(this.potion + potion);
    }
}
