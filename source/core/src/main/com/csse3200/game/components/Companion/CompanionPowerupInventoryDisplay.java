package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.configs.PowerupConfig;
import com.csse3200.game.entities.configs.PowerupConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * New, updated powerup inventory display to show the new powerup inventory
 */
public class CompanionPowerupInventoryDisplay extends Window {
    //DECLARING WINDOW STATICS
    private static final float WINDOW_WIDTH_SCALE = 0.65f;
    private static final float WINDOW_HEIGHT_SCALE = 0.65f;
    private static final float SIZE = 64f;
    private InputOverrideComponent inputOverrideComponent;

    //powerup configs used to pull sprites of the powerups
    public PowerupConfigs powerupConfigs;

    //entity companion used for triggers on updates
    Entity companion;
    private  Skin skin;

    //save the entire Powerup Inventory component
    private CompanionPowerupInventoryComponent powerupInventoryComponent;

    // local copy of PowerupsInventoryAmount
    private HashMap<PowerupType, Integer> localPowerupsInventoryAmount = new HashMap<>();

    //POWERUP SECTION
    //powerup group contains all the powerups
    Group powerupGroup = new Group();
    //first row of potions (4)
    List<PowerupConfig> firstRowPotions = new ArrayList<>();
    //second row of potions (4)
    List<PowerupConfig> secondRowPotions = new ArrayList<>();
    //initialise the powerups
    PowerupConfig deathPotion;
    PowerupConfig healthPotion;
    PowerupConfig speedPotion;
    PowerupConfig invincibilityPotion;
    PowerupConfig extraLife;
    PowerupConfig doubleCross;
    PowerupConfig doubleDamage;
    PowerupConfig snap;

    // WINDOW CONFIGURATIONS
    float startXPowerupButton = 50f; // Adjust as needed
    float spacingXPowerupButton = 100f; // Adjust as needed
    float startYFirstRowPowerupButton = 200f; // Adjust as needed
    float startYSecondRowPowerupButton = 100f; // Adjust as needed (adjusted Y position for the second row)




    /**
     * THIS FUNCTION IS REALLY THE PRE-CONSTURCTOR
     * IT is called to literally create the configurations for the PowerupInventoryDisplay to be constructedd
     * @param companion - the entity of companion object for the triggers
     * @param powerupInventoryComponent - the powerup inventory component
     * @return - the display object to go on the screen in CompanionStatsDisplay
     */
    public static CompanionPowerupInventoryDisplay createPowerupInventoryDisplay(Entity companion, CompanionPowerupInventoryComponent powerupInventoryComponent) {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new CompanionPowerupInventoryDisplay(companion, background, powerupInventoryComponent);
        //return new CompanionInventoryComponent();
    }

    /**
     * When you create the display, set it up correctly
     * @param companion - the entity of companion object for the triggers
     * @param background - some texture background for the window
     * @param powerupInventoryComponent - the powerup inventory component
     */
    public CompanionPowerupInventoryDisplay(Entity companion, Texture background, CompanionPowerupInventoryComponent powerupInventoryComponent) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));
        // load in some skin
        skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        //load in the powerup sprites
        powerupConfigs = FileLoader.readClass(PowerupConfigs.class, "configs/powerups.json");
        // save companion entity
        this.companion = companion;
        //save the entire powerup inventory component
        this.powerupInventoryComponent = powerupInventoryComponent;

        //set up the listeners
        companion.getEvents().addListener("powerupInventoryChange", this::powerupInventoryChange);

        // initialise potions
        initalisePowerupConfigs();

        // start creating the window
        setupWindow();

    }

    /**
     * First function to set up the window
     *
     */
    public void setupWindow() {
        //set up the window to be the right size and placed in the correct location
        setupWindowDimensions();

        // CREATE UI OBJECTS
        // create title at the top of window
        Table titleTable = createTitleTable();

        //create the exit button
        Table exitTable = createExitButton();

        // create powerups
        addPowerups();

        // STAGE THE TITLE AND EXIT BUTTON
        addActor(exitTable);
        addActor(titleTable);

        // stage the powerups
    }

    /**
     * The highest level powerup function which manages putting together the powerup section of the display
     */
    public void addPowerups() {
        // initialise /configure whats in what rows
        configurePowerupRows();

        // create the powerup IMAGES on the first row
        createPowerupButtons();
        // add the powerup images to the stage


        // create the power up COUNTS
        createPowerupCounts();
        // add the power up counts to the stage

        addActor(powerupGroup);

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
     * ConfigurePowerupRows is configuring which powerups go in which row, and in what order
     */
    public void configurePowerupRows() {
        //first row
        firstRowPotions.add(deathPotion);
        firstRowPotions.add(healthPotion);
        firstRowPotions.add(speedPotion);
        firstRowPotions.add(invincibilityPotion);

        //second row
        secondRowPotions.add(extraLife);
        secondRowPotions.add(doubleCross);
        secondRowPotions.add(doubleDamage);
        secondRowPotions.add(snap);
    }

    /**
     * Creates the powerup counts (also a button)
     * this is a rectangle which goes in the bottom right of the powerups
     * There is text which displays the current count of the amount
     * of that particular powerup in the inventory
     */
    public void createPowerupCounts() {
        // Source the latest powerup counts in the local powerupInventoryCount
        getLatestPowerupInventoryCounts();

        // Create and position count buttons for each potion
        float currentX = startXPowerupButton;
        float countButtonY = startYFirstRowPowerupButton - 20f; // Adjust the Y position for count buttons

        // create the count buttons for the first row
        for (PowerupConfig powerupConfig : firstRowPotions) {
            TextButton countButton = createCountButton(powerupConfig, currentX, countButtonY);
            powerupGroup.addActor(countButton);
            currentX += spacingXPowerupButton;
        }

        //create and position count buttons for second row
        currentX = startXPowerupButton;
        countButtonY = startYSecondRowPowerupButton - 20f;

        // create the count buttons for the second row
        for (PowerupConfig powerupConfig : secondRowPotions) {
            TextButton countButton = createCountButton(powerupConfig, currentX, countButtonY);
            powerupGroup.addActor(countButton);
            currentX += spacingXPowerupButton;
        }
    }

    /**
     * This function uses the powerupInventoryComponent to get the latest powerup inventory counts
     */
    public void getLatestPowerupInventoryCounts() {
        localPowerupsInventoryAmount = powerupInventoryComponent.getPowerupsInventory();
    }

    /**
     * createCountButton makes an individual count button for a powerup
     * @param powerupConfig - which button you want to get the count of
     * @param x
     * @param y
     * @return
     */
    private TextButton createCountButton(PowerupConfig powerupConfig, float x, float y) {
        //initialise the count button to zero
        TextButton countButton = new TextButton("0", skin); // Initialize count to 0
        //set scale and size
        countButton.setSize(SIZE / 2, SIZE / 2);
        //set the position on the display
        countButton.setPosition(x, y);

        // Retrieve the powerup type, and then count for the corresponding power-up type
        PowerupType powerupType = powerupConfig.type;
        int count = localPowerupsInventoryAmount.get(powerupType);
        //set the text of the button to the current count
        countButton.setText(Integer.toString(count));
        return countButton;
    }

    /**
     * This function will add all the necessary components for the first row of powerups
     */
    public void createPowerupButtons() {
        // Create and position the potion buttons in the first row
        float currentX = startXPowerupButton;
        //FIRST ROW CREATION
        for (PowerupConfig powerupConfig : firstRowPotions) {
            //create a table which contains the powerup button
            Table potionButtonTable = createPowerupButton(powerupConfig, currentX, startYFirstRowPowerupButton);
            //add that button to the powerupGroup stage
            powerupGroup.addActor(potionButtonTable);
            //adjust the X location for the next powerup
            currentX += spacingXPowerupButton;
        }
        //second row
        currentX = startXPowerupButton;
        for (PowerupConfig powerupConfig : secondRowPotions) {
            Table potionButtonTable = createPowerupButton(powerupConfig, currentX, startYSecondRowPowerupButton);
            powerupGroup.addActor(potionButtonTable);
            currentX += spacingXPowerupButton;
        }
    }

    /**
     * CreatePowerupButton is a function which creates a new "button" or image
     * for the powerup and places it at the correct location on the inventory display
     * @param powerupConfig - the configuration for each particular button (for the image)
     * @param x - x location within the display
     * @param y - y location within the display
     * @return - a ImageButton, which can be turned into a Table and added to the stage
     */
    private ImageButton createPowerupButton(PowerupConfig powerupConfig, float x, float y) {
        //create a drawable object, given the size of the texture and the textures image
        TextureRegionDrawable buttonDrawable = createTextureRegionDrawable(powerupConfig.spritePath, CompanionPowerupInventoryDisplay.SIZE);
        // new image button for the powerup
        ImageButton powerupButton = new ImageButton(buttonDrawable);
        // set the position of this powerup button to be at the given positions
        powerupButton.setPosition(x, y);

        //CREATE LISTENER FOR THE POWERUP BUTTON HERE

        // You can add listeners or other customization for potion buttons here
        return powerupButton;
    }

    /**
     * Drawing a powerup button
     * @param path - unsure
     * @param size - unsire
     * @return - unsure
     */
    private TextureRegionDrawable createTextureRegionDrawable(String path, float size) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(path));
        drawable.setMinSize(size, size);
        return drawable;
    }

    /**
     * this function updates the quantities on the UI window
     */
    public void powerupInventoryChange() {
        return;
    }


    /**
     * This function sets the windows scale up correctly
     * and places it ont he far left middle of the screen
     */
    private void setupWindowDimensions() {

        Stage stage = ServiceLocator.getRenderService().getStage();
        //setup size
        setWidth((float) (stage.getWidth() * WINDOW_WIDTH_SCALE / 2.5 + 10f));
        setHeight(stage.getHeight() * WINDOW_HEIGHT_SCALE / 2 + 10f);
        //setup position
        setPosition(
                //stage.getWidth() / 2 - getWidth() / 2 * getScaleX(),
                //stage.getWidth() * (1 - WINDOW_WIDTH_SCALE / 2),
                20,
                stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
    }

    /**
     * Creates the title at the top of the inventory
     * @return - a table which can be added to another table to show where the inventory is
     */
    private Table createTitleTable() {
        Table titleTable = new Table();
        Label title = new Label("INVENTORY", skin, "large");
        title.setColor(Color.BLACK);
        title.setFontScale(0.5F, 0.5F);
        titleTable.add(title);
        titleTable.setPosition((getWidth() * getScaleX() / 2),
                (float) (getHeight() * getScaleY() * 0.88));

        return titleTable;
    }

    /**
     * Creating an exit button on the top right of the table, to exit the upgrade tree page
     * @return - exits the tree and returns the table
     */
    private Table createExitButton() {
        TextButton exitButton = new TextButton("X", skin);
        Table table = new Table();
        table.add(exitButton).height(32f).width(32f);
        table.setPosition(((float) (getWidth() * getScaleX() * 0.91)),
                (float) (getHeight() * getScaleY() * 0.88));

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitPowerupInventory();
            }
        });
        return table;
    }

    /**
     * removes the UI element of the powerup inventory
     */
    private void exitPowerupInventory() {
        //call trigger saying the window has closed
        companion.getEvents().trigger("invertPowerupInventoryDisplayStatus");
        //remove the window
        remove();
    }

}
