package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.Companion.CompanionPowerupInventoryComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This is a window that can be added to a stage to pop up for the extractor Laboratory.
 */
public class LabWindow extends Window {
    private InputOverrideComponent inputOverrideComponent;
    private Label healthPowerupsLabel;
    private Label speedPowerupsLabel;
    private Image healthPowerupsImage;
    private Image speedPowerupsImage;
    private Entity companion = ServiceLocator.getEntityService().getCompanion();
    private int Health_powerups = companion.getComponent(CompanionPowerupInventoryComponent.class).getPowerupInventoryCount(PowerupType.HEALTH_BOOST);
    private int Speed_powerups = companion.getComponent(CompanionPowerupInventoryComponent.class).getPowerupInventoryCount(PowerupType.SPEED_BOOST);
    private TextButton[] potionButton=new TextButton[6];
    private Texture potionImage;
    private String potionName;

    public static LabWindow MakeNewLaboratory() {
        Texture background = new Texture("images/companion/lab.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background);
    }

    public LabWindow(Texture background) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));
        create();
    }

    void create(){
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.70));
        setHeight((float) (stage.getHeight() * 0.50));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));

        Texture healthPowerupTexture = new Texture("images/powerups/health_boost.png");
        Texture speedPowerupTexture = new Texture("images/powerups/speed_boost.png"); // Replace with the actual image path

        healthPowerupsImage = new Image(healthPowerupTexture);
        speedPowerupsImage = new Image(speedPowerupTexture);

        float imageSize = 32f;
        healthPowerupsImage.setSize(imageSize, imageSize);
        speedPowerupsImage.setSize(imageSize, imageSize);

        // Position the Image widgets at the top of the window
        healthPowerupsImage.setPosition(30, getHeight() - 70);
        speedPowerupsImage.setPosition(healthPowerupsImage.getX() + healthPowerupsImage.getWidth() + 25, getHeight()- 70);

        healthPowerupsLabel = new Label(": " + Health_powerups, skin);
        speedPowerupsLabel = new Label(": " + Speed_powerups, skin);

        float labelScale = 0.25f; // Adjust this value to the desired scale

        healthPowerupsLabel.setFontScale(labelScale);
        speedPowerupsLabel.setFontScale(labelScale);

        // Position the labels
        float labelOffsetX = 5; // Adjust the label offset in the X direction
        float labelOffsetY = 3; // Adjust the label offset in the Y direction

        healthPowerupsLabel.setPosition(healthPowerupsImage.getX() + healthPowerupsImage.getWidth() + labelOffsetX, getHeight() - 70 - labelOffsetY);
        speedPowerupsLabel.setPosition(speedPowerupsImage.getX() + speedPowerupsImage.getWidth() + labelOffsetX, getHeight() - 70 - labelOffsetY);
        // Add the Image widgets to the LabWindow
        addActor(healthPowerupsImage);
        addActor(speedPowerupsImage);
        addActor(healthPowerupsLabel);
        addActor(speedPowerupsLabel);
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        // Fill the entire LabWindow

        Texture[] potionImages = {
                new Texture("images/powerups/death_potion.png"),
                new Texture("images/powerups/temp_immunity.png"),
                new Texture("images/powerups/double_damage.png"),
                new Texture("images/powerups/extra_life.png"),
                new Texture("images/powerups/snap.png"),
                new Texture("images/powerups/double_cross.png")
        };

        String[] potionNames = {
                "Death", "Invincibility", "2x Damage", "Extra Life", "Snap", "Double Cross"
        };
        // Create a separate table for the "EXIT" button and position it at the bottom-right
        Table exitButtonTable = new Table();
        exitButtonTable.setFillParent(true);
        exitButtonTable.bottom().right(); // Align the table to the bottom-right corner of the screen

        TextButton returnToGameButton = new TextButton("EXIT", skin);
        exitButtonTable.add(returnToGameButton).padBottom(20).padRight(20);
        addActor(exitButtonTable);

        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                failLaboratory();
            }
        });

        for (int i = 0; i < potionImages.length; i++) {
            potionImage = potionImages[i];
            potionName = potionNames[i];


            Image potionImageWidget = new Image(potionImage);
            potionButton[i] = new TextButton(potionName, skin);
            buttonTable.add(potionImageWidget).width(100).height(64);;
            buttonTable.add(potionButton[i]).pad(20);
            int finalI = i;
            potionButton[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        PowerupType powerupType = getPowerupTypeForPotionName(potionNames[finalI]);
                        ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup", powerupType);
                        failLaboratory();

                }
            });

            if(i==2) {
                buttonTable.row();
            }
        }
        addActor(buttonTable);
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);

    }

    private PowerupType getPowerupTypeForPotionName(String potionName) {
        switch (potionName) {
            case "Death":
                return PowerupType.DEATH_POTION;
            case "Invincibility":
                return PowerupType.TEMP_IMMUNITY;
            case "2x Damage":
                return PowerupType.DOUBLE_DAMAGE;
            case "Extra Life":
                return PowerupType.EXTRA_LIFE;
            case "Snap":
                return PowerupType.SNAP;
            case "Double Cross":
                return PowerupType.DOUBLE_CROSS;
            default:
                return null;
        }
    }

    /**
     * Call this method to exit the Laboratory
     */
    private void failLaboratory() {
        remove();
    }

    /**
     * Call this method to exit the Laboratory and repair the extractor's health.
     */

    @Override
    public boolean remove() {
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}