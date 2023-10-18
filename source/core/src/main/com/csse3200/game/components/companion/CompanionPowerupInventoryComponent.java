package com.csse3200.game.components.companion;

import com.csse3200.game.components.companionweapons.CompanionWeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;




import com.csse3200.game.entities.Entity;

import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


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

    //grab the companion for triggers

    public Entity companion = null;


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
        //there has been a powerup inventory change
        sendUIPowerupInventoryChange();
    }   /**
     * getter class to get the hashmap - mostly for the UI
     * @return - hashmap of the powerup inventory amounts
     */
    public HashMap<PowerupType, Integer> getPowerupsInventory() {
        return this.powerupsInventoryAmount;
    }

    /**
     * Return the amount of powerups in the inventory
     * @param type - what type of powerup
     * @return Integer amount of powerups of that type in inventory, or NULL if that type is not inside the powerup inventory
     */
    public Integer getPowerupInventoryCount(PowerupType type) {
        return powerupsInventoryAmount.get(type);
    }


    public void removePowerupsInventoryAmount(PowerupType type, int num){powerupsInventoryAmount.put(type, getPowerupInventoryCount(type)-num);}

    /**
     * function that is trying to use a specific powerup type
     * @param type - the type of powerup to use
     */
    public void useSpecificPowerup(PowerupType type) {
        //check we have that type
        if (powerupsInventoryAmount.containsKey(type)) {
            //check we have >0 of a type
            Integer count = getPowerupInventoryCount(type);
            if (count > 0) {
                //use that powerup type
                //create the powerup component
                PowerupComponent thisPowerup = new PowerupComponent(type);

                //CHECK IF YOU ARE IN DOWNED MODE, CAN ONLY USE CERTAIN POTIONS
                if (Objects.equals(entity.getComponent(CompanionActions.class).getCompanionMode(), "COMPANION_MODE_DOWN")) {
                    if (type == PowerupType.HEALTH || type == PowerupType.SPEED_BOOST || type == PowerupType.EXTRA_LIFE) {
                        thisPowerup.applyEffect();
                        //remove one of those powerups from the powerup inventory
                        powerupsInventoryAmount.put(type,count - 1);

                        //trigger an update of the UI
                        sendUIPowerupInventoryChange();
                    }
                } else {
                    // Not in down mode, do as normal
                    //check for unique case of death potion
                    if (type == PowerupType.DEATH_POTION) {
                        //THIS HARDCODE LINE CREATES A NEW DEATH POTION FROM TRIGGER TO WEAPON CLASS
                        ServiceLocator.getEntityService().getCompanion().getEvents().trigger("changeWeapon", CompanionWeaponType.Death_Potion);
                    } else {
                        thisPowerup.applyEffect();
                    }

                    logger.info("SUCCESSFULLY CREATED AND USED POWERUPTYPE " + type.toString());

                    //remove one of those powerups from the powerup inventory
                    powerupsInventoryAmount.put(type,count - 1);

                    //trigger an update of the UI
                    sendUIPowerupInventoryChange();
                }
            }
        }
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

        //there has been a powerup inventory change
        sendUIPowerupInventoryChange();
    }

    /**
     * This function calls a trigger to the UI to update the counts for all of the powerups for the inventory UI
     */
    public void sendUIPowerupInventoryChange() {
        //call a trigger intended for the powerup inventory display, send the hashmap

        //if companion hasn't been initialised
        if (companion == null) {
            companion = ServiceLocator.getEntityService().getCompanion();
        }
        //if we have a companion entity to trigger
        if (companion != null) {
            companion.getEvents().trigger("powerupInventoryChange");
        }
    }
}
