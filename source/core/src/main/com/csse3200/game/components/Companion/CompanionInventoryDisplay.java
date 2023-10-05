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
import java.util.ArrayList;
import java.util.List;


public class CompanionInventoryDisplay extends Window {

    private static final float WINDOW_WIDTH_SCALE = 0.65f;
    private static final float WINDOW_HEIGHT_SCALE = 0.65f;
    private static final float SIZE = 64f;
    private InputOverrideComponent inputOverrideComponent;
    public PowerupConfigs powerupConfigs;
    private  Skin skin;
    private int healthPotionCount = 0;
    private TextButton deathPotionCountButton;
    private Label deathPotionCountLabel;

    Group powerupGroup = new Group();
    List<PowerupConfig> firstRowPotions = new ArrayList<>();
    List<PowerupConfig> secondRowPotions = new ArrayList<>();

    private CompanionInventoryComponent inventoryComponent;

    public static CompanionInventoryDisplay createUpgradeDisplay(CompanionInventoryComponent inventoryComponent) {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
         return new CompanionInventoryDisplay(background, inventoryComponent);
        //return new CompanionInventoryComponent();
    }

    public CompanionInventoryDisplay(Texture background, CompanionInventoryComponent inventoryComponent) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        powerupConfigs = FileLoader.readClass(PowerupConfigs.class, "configs/powerups.json");
        this.inventoryComponent = inventoryComponent;


        setupWindowDimensions();


        Table titleTable = createTitleTable();
        Table exitTable = createExitButton();
        addPowerups();

        // Add the count buttons and update their values
        //updateCountButtons();



        addActor(exitTable);
        addActor(titleTable);
//
//        // Initialize count buttons and labels
//        deathPotionCountButton = createCountButton(deathPotionConfig, startXFirstRow, countButtonY);
//        deathPotionCountLabel = createCountLabel(deathPotionCountButton, startXFirstRow, countButtonY);
//
//        // Add other count buttons and labels if needed

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    /**
     * Creates and adds all the powerups for the table
     */
    private void addPowerups() {
        PowerupConfig deathPotion = powerupConfigs.GetPowerupConfig(PowerupType.DEATH_POTION);
        PowerupConfig healthPotion = powerupConfigs.GetPowerupConfig(PowerupType.HEALTH_BOOST);
        PowerupConfig speedPotion = powerupConfigs.GetPowerupConfig(PowerupType.SPEED_BOOST);
        PowerupConfig invincibilityPotion = powerupConfigs.GetPowerupConfig(PowerupType.TEMP_IMMUNITY);
        PowerupConfig extraLife = powerupConfigs.GetPowerupConfig(PowerupType.EXTRA_LIFE);
        PowerupConfig doubleCross= powerupConfigs.GetPowerupConfig(PowerupType.DOUBLE_CROSS);
        PowerupConfig doubleDamage = powerupConfigs.GetPowerupConfig(PowerupType.DOUBLE_DAMAGE);
        PowerupConfig snap = powerupConfigs.GetPowerupConfig(PowerupType.SNAP);

        // Create lists for the first and second rows of potions
        //List<PowerupConfig> firstRowPotions = new ArrayList<>();
        firstRowPotions.add(deathPotion);
        firstRowPotions.add(healthPotion);
        firstRowPotions.add(speedPotion);
        firstRowPotions.add(invincibilityPotion);

        //List<PowerupConfig> secondRowPotions = new ArrayList<>();
        secondRowPotions.add(extraLife);
        secondRowPotions.add(doubleCross);
        secondRowPotions.add(doubleDamage);
        secondRowPotions.add(snap);

        //Group powerupGroup = new Group();

        float startXFirstRow = 50f; // Adjust as needed
        float startYFirstRow = 200f; // Adjust as needed
        float spacingXFirstRow = 100f; // Adjust as needed


        // Create and position the potion buttons in the first row
        float currentX = startXFirstRow;
        for (PowerupConfig powerupConfig : firstRowPotions) {
            Table potionButtonTable = createPowerupButton(powerupConfig, currentX, startYFirstRow);
            powerupGroup.addActor(potionButtonTable);
            currentX += spacingXFirstRow;
        }

        // Create and position the Death Potion button in the first row
        ImageButton deathPotionButton = createPowerupButton(deathPotion, startXFirstRow, startYFirstRow);
        powerupGroup.addActor(deathPotionButton);

        // Create and position the Health Potion button in the first row
        float healthPotionX = startXFirstRow + spacingXFirstRow;
        ImageButton healthPotionButton = createPowerupButton(healthPotion, healthPotionX, startYFirstRow);
        powerupGroup.addActor(healthPotionButton);

        // Create and position the Speed Potion button in the first row
        float speedPotionX = healthPotionX + spacingXFirstRow;
        ImageButton speedPotionButton = createPowerupButton(speedPotion, speedPotionX, startYFirstRow);
        powerupGroup.addActor(speedPotionButton);

        // Create and position the Invincibility Potion button in the first row
        float invincibilityPotionX = speedPotionX + spacingXFirstRow;
        ImageButton invincibilityPotionButton = createPowerupButton(invincibilityPotion, invincibilityPotionX, startYFirstRow);
        powerupGroup.addActor(invincibilityPotionButton);

        // Define the starting position and spacing for potion buttons in the second row
        float startXSecondRow = 50f; // Adjust as needed
        float startYSecondRow = 100f; // Adjust as needed (adjusted Y position for the second row)


        // Create and position the potion buttons in the second row
        currentX = startXSecondRow;
        for (PowerupConfig powerupConfig : secondRowPotions) {
            Table potionButtonTable = createPowerupButton(powerupConfig, currentX, startYSecondRow);
            powerupGroup.addActor(potionButtonTable);
            currentX += spacingXFirstRow;
        }

        // Create and position the Extra Life button in the second row
        ImageButton extraLifeButton = createPowerupButton(extraLife, startXSecondRow, startYSecondRow);
        powerupGroup.addActor(extraLifeButton);

        // Create and position the Double Cross button in the second row
        float doubleCrossX = startXSecondRow + spacingXFirstRow;
        ImageButton doubleCrossButton = createPowerupButton(doubleCross, doubleCrossX, startYSecondRow);
        powerupGroup.addActor(doubleCrossButton);

        // Create and position the Double Damage button in the second row
        float doubleDamageX = doubleCrossX + spacingXFirstRow;
        ImageButton doubleDamageButton = createPowerupButton(doubleDamage, doubleDamageX, startYSecondRow);
        powerupGroup.addActor(doubleDamageButton);

        // Create and position the Snap button in the second row
        float snapX = doubleDamageX + spacingXFirstRow;
        ImageButton snapButton = createPowerupButton(snap, snapX, startYSecondRow);
        powerupGroup.addActor(snapButton);

        // Add the potion group to your CompanionInventoryDisplay
        addActor(powerupGroup);

        // Create and position count buttons for each potion
        currentX = startXFirstRow;
        float countButtonY = startYFirstRow - 20f; // Adjust the Y position for count buttons

        for (PowerupConfig powerupConfig : firstRowPotions) {
            TextButton countButton = createCountButton(powerupConfig, currentX, countButtonY);
            powerupGroup.addActor(countButton);
            currentX += spacingXFirstRow;
        }

        countButtonY = startYSecondRow - 20f;
        currentX = startXSecondRow;

        for (PowerupConfig powerupConfig : secondRowPotions) {
            TextButton countButton = createCountButton(powerupConfig, currentX, countButtonY);
            powerupGroup.addActor(countButton);
            currentX += spacingXFirstRow;
        }

    }

    private ImageButton createPowerupButton(PowerupConfig powerupConfig, float x, float y) {
       TextureRegionDrawable buttonDrawable = createTextureRegionDrawable(powerupConfig.imagePath, CompanionInventoryDisplay.SIZE);
        ImageButton powerupButton = new ImageButton(buttonDrawable);
        //potionButton.setPosition(posX, posY);
        powerupButton.setPosition(x, y);

        // You can add listeners or other customization for potion buttons here

        return powerupButton;
    }

    private TextButton createCountButton(PowerupConfig powerupConfig, float x, float y) {
        TextButton countButton = new TextButton("0", skin); // Initialize count to 0
        countButton.setSize(SIZE / 2, SIZE / 2);
        countButton.setPosition(x, y);

        // Retrieve the count for the corresponding power-up type
        PowerupType powerupType = powerupConfig.type;
        int count = inventoryComponent.getPowerupCount(powerupType);
        countButton.setText(Integer.toString(count));


        // Add listener to update count dynamically
        countButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int updatedCount = inventoryComponent.getPowerupCount(powerupType);
                //System.out.println("Power-up Type: " + powerupType + ", Updated Count: " + updatedCount);
                updateCountButton(updatedCount, countButton);
            }
        });
        System.out.println("Listener registered for Power-up Type: " + powerupType);

        return countButton;
    }

    private void updateCountButton(int count, TextButton countButton) {
        countButton.setText(String.valueOf(count));
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
     * This function sets the windows scale up correctly
     * and places it ont he far left middle of the screen
     */
    private void setupWindowDimensions() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * WINDOW_WIDTH_SCALE / 2.5 - 40f));
        setHeight(stage.getHeight() * WINDOW_HEIGHT_SCALE / 2 + 10f);
        setPosition(
                //stage.getWidth() / 2 - getWidth() / 2 * getScaleX(),
                //stage.getWidth() * (1 - WINDOW_WIDTH_SCALE / 2),
                0,
                stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
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
                exitUpgradeTree();
            }
        });
        return table;
    }


    /**
     * removes the UI element
     */
    private void exitUpgradeTree() {
        remove();
    }

    @Override
    public boolean remove() {
        //Stop overriding input when exiting
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }


}
