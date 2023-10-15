package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

/**
 * This UI component represents the activation button on the right hand side for powerups
 *
 * ESsentially, the UI component will show individual powerups and a way to toggle between them
 *
 * it will only show the powerups which are actually stocked (>0) in the inventory
 *
 * When you click on the powerup, it will activate it, and refresh the UI component
 */
public class CompanionPowerupActivationDisplay extends UIComponent  {
    // variable declaration
    public Entity companion = ServiceLocator.getEntityService().getCompanion();
    private final String labelStyle;

    // local copy of PowerupsInventoryAmount
    private HashMap<PowerupType, Integer> localPowerupsInventoryAmount = new HashMap<>();

    // arraylist of type PowerupType is the way we will implement this
    private ArrayList<PowerupType> localPowerupActivationList = new ArrayList<>();
    private Integer powerupActivationlistIndex = 0;
    private PowerupType previousPowerupType = null;
    private static final Logger logger = LoggerFactory.getLogger(CompanionDeathScreenActions.class);

    PowerupConfig deathPotion;
    PowerupConfig healthPotion;
    PowerupConfig speedPotion;
    PowerupConfig invincibilityPotion;
    PowerupConfig extraLife;
    PowerupConfig doubleCross;
    PowerupConfig doubleDamage;
    PowerupConfig snap;
    public PowerupConfigs powerupConfigs;




    /**
     * Constructor, sets label style
     */
    public CompanionPowerupActivationDisplay() {
        labelStyle = "small";

    }

    /**
     * Create is called at the start of runtime
     *
     * It will create the UI component for the powerup activation button,
     * and make sure it is updated whenever the companions powerup inventory is updated
     *
     * Creates reusable UI styles and adds actors to the stage.
     *
     */
    @Override
    public void create() {
        super.create();

        //load in the powerup sprites
        powerupConfigs = FileLoader.readClass(PowerupConfigs.class, "configs/powerups.json");

        //fetch the updated inventory amounts
        fetchLatestPowerupInventory();

        //add the UI elements to the screen
        addActors();

        // Listen for events related to health updates
        entity.getEvents().addListener("powerupInventoryChange", this::updatePowerupInventoryActivation);
    }

    /**
     * TODO: AddActors will create the UI components required for the button to exist, as well as the navigation buttons
     *
     */
    public void addActors() {
        return;
    }


    /**
     * TODO: Add button to go left in powerup inventory list
     */
    public void addLeftButton() {
        //CREATE BUTTON AND LINK IT TO THIS FUNCTION ON PRESS
        decreaseIndexInPowerupActivationList();
    }

    /**
     * TODO: Add button to go right in powerup inventory list
     */
    public void addRightButton() {
        //CREATE BUTTON AND LINK IT TO THIS FUNCTION ON PRESS
        increaseIndexInPowerupActivationList();
    }

    /**
     * TODO: this function is called when the there are NO MORE POWERUPS IN THE INVENTORY e.g. display blank
     *
     */
    public void setUIEmptyPowerupInventory() {
        return;
    }

    /**
     * TODO: This function is called to get the specific powerupType required to display. It should now set some sort
     * TODO: UI element to that display type
     */
    public void setUISpecificPowerup() {
        // given the current index, display a specific type of powerup
        PowerupType displayThisType = localPowerupActivationList.get(powerupActivationlistIndex);
        // you need to fetch the correct button and put it on the screen

        //PRINT THE NEW TYPE THAT IS SELECTED
        logger.info(localPowerupActivationList.toString());
        logger.info(displayThisType.toString());
    }
    /**
     * Just gets the configs of each powerup
     */
    public void initalisePowerupConfigs() {
        this.deathPotion = powerupConfigs.GetPowerupConfig(PowerupType.DEATH_POTION);
        this.healthPotion = powerupConfigs.GetPowerupConfig(PowerupType.HEALTH_BOOST);
        this.speedPotion = powerupConfigs.GetPowerupConfig(PowerupType.SPEED_BOOST);
        this.invincibilityPotion = powerupConfigs.GetPowerupConfig(PowerupType.TEMP_IMMUNITY);
        this.extraLife = powerupConfigs.GetPowerupConfig(PowerupType.EXTRA_LIFE);
        this.doubleCross= powerupConfigs.GetPowerupConfig(PowerupType.DOUBLE_CROSS);
        this.doubleDamage = powerupConfigs.GetPowerupConfig(PowerupType.DOUBLE_DAMAGE);
        this.snap = powerupConfigs.GetPowerupConfig(PowerupType.SNAP);
    }


    /**
     * This function is called whenever the inventory amounts are updated
     *
     */
    public void updatePowerupInventoryActivation() {
        // fetch the updated inventory
        fetchLatestPowerupInventory();

        // re-create arrayList of the powerups available
        updateArrayListOfPowerupInventory();

        //make sure there is a powerup to be activated, otherwise display nothing
        if (localPowerupActivationList.isEmpty()) {
            setUIEmptyPowerupInventory();
        } else {
            //there is an element in the list, display the power up at the current index
            setUISpecificPowerup();
        }
    }

    /**
     * Function called when "right" button is pressed, to iterate UP the list of powerups available
     *
     * if the list is empty, this does nothing
     *
     * if list is one, it'll refresh the UI object to the same powerup
     */
    public void increaseIndexInPowerupActivationList() {
        if (!localPowerupActivationList.isEmpty()) {
            //increase by 1
            powerupActivationlistIndex++;
            //if you've gone outside the bounds
            if (powerupActivationlistIndex > localPowerupActivationList.size()) {
                powerupActivationlistIndex = 0;
            }
            //update the saved power type
            updateSavedPowerupType();
            // update the UI
            setUISpecificPowerup();
        }
    }

    /**
     * Function called when "left" button is pressed, to iterate DOWN the list of powerups available
     */
    public void decreaseIndexInPowerupActivationList() {
        if (!localPowerupActivationList.isEmpty()) {
            //increase by 1
            powerupActivationlistIndex--;
            //if you've gone outside the bounds
            if (powerupActivationlistIndex < 0) {
                //set the index to the size of the list, minus one for zero index
                powerupActivationlistIndex = localPowerupActivationList.size() - 1;
            }
            //save the new type
            updateSavedPowerupType();
            // update the UI
            setUISpecificPowerup();
        }
    }

    /**
     * PREREQ: Can only call this function if the list is > 0 and activation index is all correct
     */
    public void updateSavedPowerupType() {
        previousPowerupType = localPowerupActivationList.get(powerupActivationlistIndex);
    }





    /**
     * Function to be called when any type of powerup on the screen which is not null is pressed
     */
    public void pressedSpecificPowerupToBeUsed() {
        // given the current index, display a specific type of powerup
        PowerupType displayThisType = localPowerupActivationList.get(powerupActivationlistIndex);
        // this will go into the inventory, check we have one, and then activate the powerup, then call changePowerupInventory
        ServiceLocator.getEntityService().getCompanion().getComponent(CompanionPowerupInventoryComponent.class).useSpecificPowerup(displayThisType);
    }




    /**
     * This function will reset the powerups in the linkedList
     * Gets a brand new arrayList, which contains all the types of powerups which are AVAILABLE TO USE
     *
     */
    public void updateArrayListOfPowerupInventory() {
        //reset the list
        localPowerupActivationList = new ArrayList<>();

        // iterate through the hashmap
        for (Map.Entry<PowerupType, Integer> entry : localPowerupsInventoryAmount.entrySet()) {
            PowerupType type = entry.getKey();
            Integer count = entry.getValue();
            // if there are more than one of a particular powerup, add it to the powerupActivationList
            if (count > 0) {
                localPowerupActivationList.add(type);
            }
        }

        updateSavedIndex();
    }

    /**
     * UpdatesavedIndex basically
     *
     * If you have saved a type before, see if that type is still in the list. if it is, go to that index.
     *
     * If that type is not in the list, just reset the index to zero and save the type there (if there is a type to save)
     *
     */
    public void updateSavedIndex() {
        //if you have something saved
        if (previousPowerupType!= null) {
            // if the list is not empty
            if (!localPowerupActivationList.isEmpty()) {
                // does the list has my previous saved type in the list, set the index
                if (localPowerupActivationList.contains(previousPowerupType)) {
                    // set the current index to the powerup type of the previously activated index
                    powerupActivationlistIndex = localPowerupActivationList.indexOf(previousPowerupType);
                } else {
                    //set the previous powerup type to the first index of the arraylist
                    powerupActivationlistIndex = 0;
                    previousPowerupType = localPowerupActivationList.get(powerupActivationlistIndex);
                }
            } else {
                // list is empty, set previous saved type to null
                previousPowerupType = null;
            }
        } else {
            // you haven't saved anything

            // if the list is not empty, make the saved whatever you are looking at
            if (!localPowerupActivationList.isEmpty()) {
                //set the previous powerup type to the first index of the arraylist
                powerupActivationlistIndex = 0;
                previousPowerupType = localPowerupActivationList.get(powerupActivationlistIndex);
            } else {
                // if there is an empty list, do nothing and keep it null
            }
        }
    }

    /**
     * This function is called to get the latest inventory amounts from the powerup inventory component
     */
    public void fetchLatestPowerupInventory() {
        localPowerupsInventoryAmount = ServiceLocator.getEntityService().getCompanion().getComponent(CompanionPowerupInventoryComponent.class).getPowerupsInventory();
    }

    /**
     * Draw
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Code for drawing UI elements and updating the projection matrix.
    }

    /**
     * remove all labels form the screen when disposing
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
