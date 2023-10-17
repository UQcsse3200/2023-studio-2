package com.csse3200.game.components.companion;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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

    PowerupConfig deathPotion;
    PowerupConfig healthPotion;
    PowerupConfig speedPotion;
    PowerupConfig invincibilityPotion;
    PowerupConfig extraLife;
    PowerupConfig doubleCross;
    PowerupConfig doubleDamage;
    PowerupConfig snap;
    Button powerupActivationButton;
    Label powerupLabel;
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
        //create the powerup label element
        powerupLabel = new Label("Powerups", skin,"thick");
        powerupLabel.setColor(Color.BLACK);
        powerupLabel.setFontScale(0.2f, 0.2f);

        addPowerupActivationButton();
        // add navigation buttons
        addLeftButton();
        addRightButton();

        //stage UI elements
        //stage.addActor(powerupActivationButton);
        stage.addActor(rightButton);
        stage.addActor(leftButton);

    }

    /**
     * This funciton creates the powerup activation button in the centre
     */
    public void addPowerupActivationButton() {
        //create the new button
        powerupActivationButton = new Button(skin);
        // configure size and placement of the button
        powerupActivationButton.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        powerupActivationButton.setPosition(1675f,270f);
        powerupActivationButton.setSize(187f, 95f);

        //create the potion IMAGE element and make sure its showing the latest, correct image
        updatePowerupImageToLatest();

        //UI table element to combine potion label and image
        powerupActivationButtonTable = new Table();

        //add image
        powerupActivationButtonTable.add(displayedPowerupImage).size(64, 64).row();
        // add label
        powerupActivationButtonTable.add(powerupLabel);

        // add the UI elements to the button
        powerupActivationButton.add(powerupActivationButtonTable).width(150).height(150);

        //BIND key presses on the button to a potential activation of powerup
        powerupActivationButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // attempt to use a powerup, if there is one currently selected
                attemptedPowerupActivationButtonPress();
            }
        });
        stage.addActor(powerupActivationButton);
    }


    /**
     * TODO: Add button to go left in power-up inventory list
     */
    public void addLeftButton() {
        leftButton = new TextButton("prev", skin);
        leftButton.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        leftButton.setPosition(1620f,300f);
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
        rightButton.setPosition(1870f, 300f);
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
     * This function is called by addPowerupActivationButton
     * It returns the image which will represent the powerup which is currently selected
     *
     * It will have to check if there is a powerup at the current index, if not return an empty image
     * If there is a powerup, fetch the correct image and return it
     */
    public void updatePowerupImageToLatest() {
        //check if there is a powerup currently
        PowerupType displayThisType = getSelectedPowerupType();


        if (displayThisType == null) {
            //create image
            displayedPowerupImage = new Image();
            //update the label text
            powerupLabel.setText("Powerups");
        } else {
            logger.info("attempting to display" + displayThisType.toString());
            //now big IF ELSE statement mapping a powerup type to the correct config file
            if (displayThisType == PowerupType.DEATH_POTION) {
                displayedPowerupImage = new Image(new Texture(deathPotion.spritePath));
                //update the label text
                powerupLabel.setText("Death");
            } else if (displayThisType == PowerupType.HEALTH_BOOST) {
                displayedPowerupImage = new Image(new Texture(healthPotion.spritePath));
                //update the label text
                powerupLabel.setText("Health");
            } else if (displayThisType == PowerupType.SNAP) {
                displayedPowerupImage = new Image(new Texture(snap.spritePath));
                //update the label text
                powerupLabel.setText("Snap");
            } else if (displayThisType == PowerupType.DOUBLE_CROSS) {
                displayedPowerupImage = new Image(new Texture(doubleCross.spritePath));
                //update the label text
                powerupLabel.setText("Double Cross");
            } else if (displayThisType == PowerupType.EXTRA_LIFE) {
                displayedPowerupImage = new Image(new Texture(extraLife.spritePath));
                //update the label text
                powerupLabel.setText("Extra Life");
            } else if (displayThisType == PowerupType.DOUBLE_DAMAGE) {
                displayedPowerupImage = new Image(new Texture(doubleDamage.spritePath));
                //update the label text
                powerupLabel.setText("Double Damage");
            } else if (displayThisType == PowerupType.SPEED_BOOST) {
                displayedPowerupImage = new Image(new Texture(speedPotion.spritePath));
                //update the label text
                powerupLabel.setText("Speed Boost");
            } else if (displayThisType == PowerupType.TEMP_IMMUNITY) {
                displayedPowerupImage = new Image(new Texture(invincibilityPotion.spritePath));
                //update the label text
                powerupLabel.setText("Temporary Immunity");
            }
        }
    }

    /**
     * This function is called whenever the powerup activation UI needs to be refreshed
     */
    public void refreshPowerupActivationButtonUI() {
        /// CLEAR THE BUTTON
        powerupActivationButton.remove();

        //re-add the button
        addPowerupActivationButton();
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



        //refresh UI component
        refreshPowerupActivationButtonUI();
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
            if (powerupActivationlistIndex >= localPowerupActivationList.size()) {
                powerupActivationlistIndex = 0;
            }
            //update the saved power type
            updateSavedPowerupType();
            //update UI
            refreshPowerupActivationButtonUI();
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
            //update UI
            refreshPowerupActivationButtonUI();
        }
    }

    /**
     * PREREQ: Can only call this function if the list is > 0 and activation index is all correct
     */
    public void updateSavedPowerupType() {
        previousPowerupType = localPowerupActivationList.get(powerupActivationlistIndex);
    }

    /**
     * Function to be called when the powerup activation button is pressed
     */
    public void attemptedPowerupActivationButtonPress() {
        // get current powerup type. if null, do nothing,
        //check if there is a powerup currently
        PowerupType displayThisType = getSelectedPowerupType();

        if (displayThisType == null) {
            return;
        } else {
            // this will go into the inventory, check we have one, and then activate the powerup, then call changePowerupInventory
            ServiceLocator.getEntityService().getCompanion().getComponent(CompanionPowerupInventoryComponent.class).useSpecificPowerup(displayThisType);
        }
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
     * Function is called to get the currently selected powerup type
     * @return - null if no powerups available, poweruptype if one is available.
     */
    public PowerupType getSelectedPowerupType() {
        // check there is something in list
        if (localPowerupActivationList.isEmpty()) {
            return null;
        } else {
            return localPowerupActivationList.get(powerupActivationlistIndex);
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