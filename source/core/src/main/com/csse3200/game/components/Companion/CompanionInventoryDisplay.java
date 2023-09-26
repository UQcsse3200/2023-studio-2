package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.PotionType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PotionConfig;
import com.csse3200.game.entities.configs.PotionConfigs;
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
    private PotionConfigs potionConfigs;
    private  Entity player;
    private  Skin skin;

    public static CompanionInventoryDisplay createUpgradeDisplay() {
        Texture background =
                ServiceLocator.getResourceService().getAsset("images/upgradetree/background.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
         return new CompanionInventoryDisplay(background);
        //return new CompanionInventoryComponent();
    }

    public CompanionInventoryDisplay(Texture background) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        potionConfigs = FileLoader.readClass(PotionConfigs.class, "configs/potions.json");


        setupWindowDimensions();


        Table titleTable = createTitleTable();
        Table exitTable = createExitButton();
        addPotions();
        addActor(exitTable);
        addActor(titleTable);

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    private void addPotions() {
        PotionConfig deathPotion = potionConfigs.GetPotionConfig(PotionType.DEATH_POTION);
        PotionConfig healthPotion = potionConfigs.GetPotionConfig(PotionType.HEALTH_POTION);
        PotionConfig speedPotion = potionConfigs.GetPotionConfig(PotionType.SPEED_POTION);
        PotionConfig invincibilityPotion = potionConfigs.GetPotionConfig(PotionType.INVINCIBILITY_POTION);
        PotionConfig extraLife = potionConfigs.GetPotionConfig(PotionType.EXTRA_LIFE);
        PotionConfig doubleCross= potionConfigs.GetPotionConfig(PotionType.DOUBLE_CROSS);
        PotionConfig doubleDamage = potionConfigs.GetPotionConfig(PotionType.DOUBLE_DAMAGE);
        PotionConfig snap = potionConfigs.GetPotionConfig(PotionType.SNAP);

        // Create lists for the first and second rows of potions
        List<PotionConfig> firstRowPotions = new ArrayList<>();
        firstRowPotions.add(deathPotion);
        firstRowPotions.add(healthPotion);
        firstRowPotions.add(speedPotion);
        firstRowPotions.add(invincibilityPotion);

        List<PotionConfig> secondRowPotions = new ArrayList<>();
        secondRowPotions.add(extraLife);
        secondRowPotions.add(doubleCross);
        secondRowPotions.add(doubleDamage);
        secondRowPotions.add(snap);

        Group potionGroup = new Group();

        float startXFirstRow = 50f; // Adjust as needed
        float startYFirstRow = 200f; // Adjust as needed
        float spacingXFirstRow = 100f; // Adjust as needed


        // Create and position the potion buttons in the first row
        float currentX = startXFirstRow;
        for (PotionConfig potionConfig : firstRowPotions) {
            Table potionButtonTable = createPotionButton(potionConfig, currentX, startYFirstRow);
            potionGroup.addActor(potionButtonTable);
            currentX += spacingXFirstRow;
        }

        // Create and position the Death Potion button in the first row
        ImageButton deathPotionButton = createPotionButton(deathPotion, startXFirstRow, startYFirstRow);
        potionGroup.addActor(deathPotionButton);

        // Create and position the Health Potion button in the first row
        float healthPotionX = startXFirstRow + spacingXFirstRow;
        ImageButton healthPotionButton = createPotionButton(healthPotion, healthPotionX, startYFirstRow);
        potionGroup.addActor(healthPotionButton);

        // Create and position the Speed Potion button in the first row
        float speedPotionX = healthPotionX + spacingXFirstRow;
        ImageButton speedPotionButton = createPotionButton(speedPotion, speedPotionX, startYFirstRow);
        potionGroup.addActor(speedPotionButton);

        // Create and position the Invincibility Potion button in the first row
        float invincibilityPotionX = speedPotionX + spacingXFirstRow;
        ImageButton invincibilityPotionButton = createPotionButton(invincibilityPotion, invincibilityPotionX, startYFirstRow);
        potionGroup.addActor(invincibilityPotionButton);

        // Define the starting position and spacing for potion buttons in the second row
        float startXSecondRow = 50f; // Adjust as needed
        float startYSecondRow = 100f; // Adjust as needed (adjusted Y position for the second row)


        // Create and position the potion buttons in the second row
        currentX = startXSecondRow;
        for (PotionConfig potionConfig : secondRowPotions) {
            Table potionButtonTable = createPotionButton(potionConfig, currentX, startYSecondRow);
            potionGroup.addActor(potionButtonTable);
            currentX += spacingXFirstRow;
        }

        // Create and position the Extra Life button in the second row
        ImageButton extraLifeButton = createPotionButton(extraLife, startXSecondRow, startYSecondRow);
        potionGroup.addActor(extraLifeButton);

        // Create and position the Double Cross button in the second row
        float doubleCrossX = startXSecondRow + spacingXFirstRow;
        ImageButton doubleCrossButton = createPotionButton(doubleCross, doubleCrossX, startYSecondRow);
        potionGroup.addActor(doubleCrossButton);

        // Create and position the Double Damage button in the second row
        float doubleDamageX = doubleCrossX + spacingXFirstRow;
        ImageButton doubleDamageButton = createPotionButton(doubleDamage, doubleDamageX, startYSecondRow);
        potionGroup.addActor(doubleDamageButton);

        // Create and position the Snap button in the second row
        float snapX = doubleDamageX + spacingXFirstRow;
        ImageButton snapButton = createPotionButton(snap, snapX, startYSecondRow);
        potionGroup.addActor(snapButton);

        // Add the potion group to your CompanionInventoryDisplay
        addActor(potionGroup);

        // Create and position count buttons for each potion
        currentX = startXFirstRow;
        float countButtonY = startYFirstRow - 20f; // Adjust the Y position for count buttons

        for (PotionConfig potionConfig : firstRowPotions) {
            TextButton countButton = createCountButton(potionConfig, currentX, countButtonY);
            potionGroup.addActor(countButton);
            currentX += spacingXFirstRow;
        }

        countButtonY = startYSecondRow - 20f;
        currentX = startXSecondRow;

        for (PotionConfig potionConfig : secondRowPotions) {
            TextButton countButton = createCountButton(potionConfig, currentX, countButtonY);
            potionGroup.addActor(countButton);
            currentX += spacingXFirstRow;
        }

    }

    private ImageButton createPotionButton(PotionConfig potionConfig, float x, float y) {
       TextureRegionDrawable buttonDrawable = createTextureRegionDrawable(potionConfig.imagePath, CompanionInventoryDisplay.SIZE);
        ImageButton potionButton = new ImageButton(buttonDrawable);
        //potionButton.setPosition(posX, posY);
        potionButton.setPosition(x, y);

        // You can add listeners or other customization for potion buttons here

        return potionButton;
    }

    private TextButton createCountButton(PotionConfig potionConfig, float x, float y) {
        TextButton countButton = new TextButton("0", skin); // Initialize count to 0
        countButton.setSize(SIZE / 2, SIZE / 2);
        countButton.setPosition(x, y);
        return countButton;
    }


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

    private void setupWindowDimensions() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth(stage.getWidth() * WINDOW_WIDTH_SCALE / 2 - 20f);
        setHeight(stage.getHeight() * WINDOW_HEIGHT_SCALE / 2 + 10f);
        setPosition(
                //stage.getWidth() / 2 - getWidth() / 2 * getScaleX(),
                stage.getWidth() * (1 - WINDOW_WIDTH_SCALE / 2),
                stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
    }

    private TextureRegionDrawable createTextureRegionDrawable(String path, float size) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(path));
        drawable.setMinSize(size, size);
        return drawable;
    }


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
