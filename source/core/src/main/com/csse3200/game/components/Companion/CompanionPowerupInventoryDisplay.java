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

    public PowerupConfigs powerupConfigs;

    Entity companion;
    private  Skin skin;

    //save the entire Powerup Inventory component
    private CompanionPowerupInventoryComponent powerupInventoryComponent;

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

        // STAGE THE TITLE AND EXIT BUTTON
        addActor(exitTable);
        addActor(titleTable);

        // stage the powerups
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
        setWidth((float) (stage.getWidth() * WINDOW_WIDTH_SCALE / 2.5 - 40f));
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
