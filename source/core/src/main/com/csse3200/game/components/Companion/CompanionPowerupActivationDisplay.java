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
 * Essentially, the UI component will show individual powerups and a way to toggle between them
 *
 * it will only show the powerups which are actually stocked (>0) in the inventory
 *
 * When you click on the powerup, it will activate it, and refresh the UI component
 */
public class CompanionPowerupActivationDisplay extends UIComponent {
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
     * Creates reusable UI styles and adds actors to the stage.
     *
     */
    @Override
    public void create() {
        super.create();

        //load in the powerup sprites
        powerupConfigs = FileLoader.readClass(PowerupConfigs.class, "configs/powerups.json");
        initalisePowerupConfigs();

        //fetch the updated inventory amounts
        fetchLatestPowerupInventory();

        // re-create arrayList of the power ups available
        updateArrayListOfPowerupInventory();

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
     * TODO: Add button to go left in power-up inventory list
     */
    public void addLeftButton() {
        return;
    }

    /**
     * TODO: Add button to go right in power-up inventory list
     */
    public void addRightButton() {
        return;
    }

    /**
     * TODO: this function is called when the there are NO MORE POWERUPS IN THE INVENTORY e.g. display blank
     * TODO: This function will set the UI element which contains the powerup to A BLANK DEFAULT STATE
     */
    public void setUIEmptyPowerupInventory() {
        return;
    }

    /**
     * TODO: This function is called to get the specific powerupType required to display. It should now set some sort
     * TODO: UI element to that display type of powerup, and simply bind the ON CLICK event to the pressedSpecificPowerupToBeUsed
     */
    public void setUISpecificPowerup() {
        return;
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
     */
    public void updatePowerupInventoryActivation() {
        // fetch the updated inventory
        fetchLatestPowerupInventory();
    }

    /**
     * Function called when "right" button is pressed, to iterate UP the list of power-ups available
     * if the list is empty, this does nothing
     * if list is one, it'll refresh the UI object to the same power-up
     */
    public void increaseIndexInPowerupActivationList() {
        return;
    }

    /**
     * Function called when "left" button is pressed, to iterate DOWN the list of powerups available
     */
    public void decreaseIndexInPowerupActivationList() {
        return;
    }

    /**
     * PREREQ: Can only call this function if the list is > 0 and activation index is all correct
     */
    public void updateSavedPowerupType() {
        return;
    }

    /**
     * Function to be called when any type of powerup on the screen which is not null is pressed
     */
    public void pressedSpecificPowerupToBeUsed() {
        return;
    }

    /**
     * This function will reset the powerups in the linkedList
     * Gets a brand new arrayList, which contains all the types of powerups which are AVAILABLE TO USE
     *
     */
    public void updateArrayListOfPowerupInventory() {
        return;
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
        return;
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