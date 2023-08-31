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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
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

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds UI elements such as buttons and title to the main menu display.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        // Display game title image
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset("images/escape-earth2.png", Texture.class));
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);

        // Create buttons for various menu options
        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Load", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton miniBtn = new TextButton("Space Minigame", skin);
        TextButton extractorBtn = new TextButton("Extractor Minigame", skin);
        TextButton spaceMapBtn = new TextButton("Space Map", skin);

        // Attach listeners to buttons
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
                    }
                });

        loadBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Load button clicked");
                        entity.getEvents().trigger("load");
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

        spaceMapBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Space Map button clicked");
                        entity.getEvents().trigger("space map");
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

        // Arrange UI elements in a table layout
        table.add(titleImage);
        table.row();
        table.add(startBtn).padTop(30f).padLeft(1200f);
        table.row();
        table.add(loadBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(settingsBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(miniBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(extractorBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(spaceMapBtn).padTop(15f).padLeft(1200f);
        table.row();
        table.add(exitBtn).padTop(15f).padLeft(1200f);

        stage.addActor(titleImage);
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
