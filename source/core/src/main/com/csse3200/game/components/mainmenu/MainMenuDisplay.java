/**
 * MainMenuDisplay is a UI component responsible for displaying the main menu of the game.
 * It provides buttons for starting the game, loading a saved game, accessing settings,
 * and exiting the game. Additionally, it offers options to access space and extractor mini-games.
 */
package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.LoadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The UI component responsible for rendering and managing the main menu interface.
 * It provides options for starting, loading, configuring settings, and exiting the game.
 * It also includes options to access space and extractor mini-games.
 */
public class MainMenuDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    public int frame;
    private Image transitionFrames;

    @Override
    public void create() {
        frame=1;
        super.create();
        transitionFrames = new Image();
        addActors();
    }
    //created a checkbox group for single/multi player functionality
    ButtonGroup<CheckBox> checkBoxGroup = new ButtonGroup<>();

    /**
     * Adds UI elements such as buttons and title to the main menu display.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        // Display game title image
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset("images/menu/main-menu.png", Texture.class));
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);

        boolean validLoad = LoadUtils.pathExists(LoadUtils.SAVE_PATH, LoadUtils.GAMESTATE_FILE);

        // Create buttons for various menu options
        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Load Save", skin, validLoad ? "default" : "invalid");
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton miniBtn = new TextButton("Space Minigame", skin);
        TextButton extractorBtn = new TextButton("Extractor Minigame", skin);
        TextButton upgradeShip = new TextButton("Upgrade Ship", skin);
        TextButton tutorialBtn = new TextButton("Tutorial", skin);
        TextButton brickBreakerBtn = new TextButton("brick breaker minigame", skin);

        // Attach listeners to buttons
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start", validLoad);
                    }
                });
        loadBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Load button clicked");
                        entity.getEvents().trigger("load", validLoad);
                    }
                });
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        miniBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Space Minigame button clicked");
                        entity.getEvents().trigger("space minigame");
                    }
                });

        extractorBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Extractor Minigame button clicked");
                        entity.getEvents().trigger("extractor minigame");
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        upgradeShip.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Upgrade Ship button clicked");
                        entity.getEvents().trigger("upgrade shop");
                    }
                });

        tutorialBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tutorial button clicked");
                        entity.getEvents().trigger("tutorial");
                    }
                }
        );

        brickBreakerBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("brick breaker button clikced");
                        entity.getEvents().trigger("brick breaker minigame");
                    }
                });



        // Arrange UI elements in a table layout
        table.add(titleImage);
        table.row();
        table.add(startBtn).padTop(30f).padLeft(1200f);
        table.row();
        table.add(loadBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(settingsBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(tutorialBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(miniBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(extractorBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(exitBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(upgradeShip).padTop(15f).padLeft(1200f);
        table.row();
        table.add(brickBreakerBtn).padTop(15f).padLeft(1200f);
        table.row();
        stage.addActor(titleImage);

        stage.addActor(transitionFrames);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}