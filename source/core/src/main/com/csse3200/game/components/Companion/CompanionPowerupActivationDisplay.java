package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
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
    Button powerupActivationButton;
    Label potionLabel;
    TextButton leftButton;
    TextButton rightButton;
    Image displayedPowerupImage;
    Table powerupActivationButtonTable;

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
        //add the powerup activation button (Main button)
        addPowerupActivationButton();
        // add navigation buttons
        addLeftButton();
        addRightButton();

        //stage UI elements
        stage.addActor(powerupActivationButton);
        stage.addActor(rightButton);
        stage.addActor(leftButton);

    }

    /**
     * This funciton creates the powerup activation button in the centre
     */
    public void addPowerupActivationButton() {
        //create the button
        powerupActivationButton = new Button(skin);
        // configure size and placement of the button
        powerupActivationButton.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        powerupActivationButton.setPosition(1405f,280f);
        powerupActivationButton.setSize(187f, 95f);

        //create the potion label element
        potionLabel = new Label("Potion", skin,labelStyle);
        potionLabel.setColor(Color.BLACK);
        potionLabel.setFontScale(0.2f, 0.2f);

        //create the potion IMAGE element and make sure its bound to the arrayList
        displayedPowerupImage = new Image();

        //UI table element to combine potion label and image
        powerupActivationButtonTable = new Table();

        //add image
        powerupActivationButtonTable.add(displayedPowerupImage).size(64, 64).row();
        // add label
        powerupActivationButtonTable.add(potionLabel);

        // add the UI elements to the button
        powerupActivationButton.add(powerupActivationButtonTable).width(150).height(150);

        //BIND key presses on the button to a potential activation of powerup
        powerupActivationButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //nothing
            }
        });
    }


    /**
     * TODO: Add button to go left in power-up inventory list
     */
    public void addLeftButton() {
        leftButton = new TextButton("prev", skin);
        leftButton.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        leftButton.setPosition(1350f,310f);
        leftButton.setSize(50f, 45f);
        // bind on click button event to a change in the list
        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // go left on the powerup activation index
                decreaseIndexInPowerupActivationList();
            }
        });
    }

    /**
     * TODO: Add button to go right in power-up inventory list
     */
    public void addRightButton() {
        rightButton = new TextButton("next", skin);
        rightButton.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        rightButton.setPosition(1595f, 310f);
        rightButton.setSize(50f, 45f);
        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // CALL GO RIGHT BUTTON PRESS
                increaseIndexInPowerupActivationList();
            }
        });
    }


    /**
     * TODO: this function is called when the there are NO MORE POWERUPS IN THE INVENTORY e.g. display blank
     * TODO: This function will set the UI element which contains the powerup to A BLANK DEFAULT STATE
     */
    public void setUIEmptyPowerupInventory() {
        displayedPowerupImage = new Image();
        refreshPowerupActivationButtonUI();
    }

    /**
     * TODO: This function is called to get the specific powerupType required to display. It should now set some sort
     * TODO: UI element to that display type of powerup, and simply bind the ON CLICK event to the pressedSpecificPowerupToBeUsed
     */
    public void setUISpecificPowerup() {

        // given the current index, display a specific type of powerup
        //PowerupType displayThisType = localPowerupActivationList.get(powerupActivationlistIndex);
        // you need to fetch the correct button and put it on the screen

        //powerupImage = new Image(new Texture(deathPotion.spritePath));
        //displayedPowerupImage.getImageHeight == 0 when its initialised to nothing.

        // MAKE SURE BUTTON IS BOUND TO THIS FUNCTION
        //pressedSpecificPowerupToBeUsed();

        //PRINT THE NEW TYPE THAT IS SELECTED
        logger.info(localPowerupActivationList.toString());
        //logger.info(displayThisType.toString());
    }

    /**
     * This function is called whenever the powerup activation UI needs to be refreshed
     */
    public void refreshPowerupActivationButtonUI() {
        ///POTENTIALLY CLEAR THE BUTTON


        //UI table element to combine potion label and image
        powerupActivationButtonTable = new Table();
        // add label
        powerupActivationButtonTable.add(potionLabel);
        // add image
        powerupActivationButtonTable.add(displayedPowerupImage).size(64,64).row();

        // add the UI elements to the button
        powerupActivationButton.add(powerupActivationButtonTable).width(150).height(150);
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
     * Function called when "right" button is pressed, to iterate UP the list of power-ups available
     * if the list is empty, this does nothing
     * if list is one, it'll refresh the UI object to the same power-up
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