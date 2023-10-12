package com.csse3200.game.components.Companion;

import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;




import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfigs;

import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The new powerup inventory component.
 * This component will manage all the powerup's in the inventory
 *
 * It has been created to simplify the powerup inventory component to be far simpler
 * This will interface with the UI, and reflect the accurat amount of powerups on the companion and the player
 */
public class CompanionPowerupInventoryComponent extends Component {

    // Add a HashMap to store counts for each power-up type. DOESN'T DO ANYTHING
    private final HashMap<PowerupType, Integer> powerupsInventoryAmount = new HashMap<>();

    //setup loger
    private static final Logger logger = LoggerFactory.getLogger(CompanionDeathScreenActions.class);


    /**
     * Constructor
     * Create a hash map which stores all the counts for the powerups in the inventory
     *
     */
    public CompanionPowerupInventoryComponent() {
        //initialise the hashmap
        initialisePowerupInventoryAmount();
        logger.info(powerupsInventoryAmount.toString());
    }

    /**
     * Set up a record of all the different powerup types
     *
     * Set the value of powerups in the inventory to zero
     */
    public void initialisePowerupInventoryAmount() {
        //loop through all the different powerup types
        for (PowerupType type : PowerupType.values()) {
            //set its value to zero
            powerupsInventoryAmount.put(type, 0);
        }
    }

    /**
     * Return the amount of powerups in the inventory
     * @param type - what type of powerup
     * @return Integer amount of powerups of that type in inventory, or NULL if that type is not inside the powerup inventory
     */
    public Integer getPowerupInventoryCount(PowerupType type) {
        return powerupsInventoryAmount.get(type);
    }



    /**
     * Adding powerups to inventory actually works.
     *
     * ADDS A NUMBER TO THE INVENTORY, REGARDLESS OF HOW BIG IT IS
     *
     * MAY NEED TO ADD A CHECK TO SEE IF IT IS TOO BIG eg. >10 for UI purposes
     *
     *
     * Add a certain type of powerup to the powerup inventory
     * @param type - a powerup type to be added
     *            NO RETURN
     */
    public void addPowerupToPowerupInventory(PowerupType type) {
        //if the result is not null, then that type is inside the inventory
        if (getPowerupInventoryCount(type) != null) {
            //now add one of the type to that type
            Integer currentPowerupTypeCount = getPowerupInventoryCount(type);

            // update the old map value to the old value + 1
            powerupsInventoryAmount.put(type, currentPowerupTypeCount + 1);
        }
        //WHENEVER A POTION PICKED UP, SHOW NEW COUNTS
        logger.info(powerupsInventoryAmount.toString());

        //CALL AN UPDATE TO THE UI
    }

    /**
     * This is going to be called by companion key input
     *
     * If you press n, at any point in time, itll check if you have any space left for a death potion
     *
     * if you do, it'll create one.
     *
     * if not, it wont
     */
    public void tryCreateDeathPowerupFromPowerupInventory() {
        //if the result is not null, then that type is inside the inventory
        if (getPowerupInventoryCount(PowerupType.DEATH_POTION) != null) {
            //now CHECK if there is at least 1 death potion in the inventory
            Integer currentPowerupTypeCount = getPowerupInventoryCount(PowerupType.DEATH_POTION);
            //if there are more than 0
            if (currentPowerupTypeCount > 0) {
                //remove one quantity of death potion from the inventory
                powerupsInventoryAmount.put(PowerupType.DEATH_POTION, currentPowerupTypeCount - 1);

                //spawn a death potion weapon
                //THIS HARDCODE LINE CREATES A NEW DEATH POTION FROM TRIGGER TO WEAPON CLASS
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("changeWeapon", CompanionWeaponType.Death_Potion);
                //call PowerupInventoryAmountChange
            }
        }
    }


}
