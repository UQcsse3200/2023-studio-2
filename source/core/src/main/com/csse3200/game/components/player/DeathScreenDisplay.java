/**
 * MainMenuDisplay is a UI component responsible for displaying the main menu of the game.
 * It provides buttons for starting the game, loading a saved game, accessing settings,
 * and exiting the game. Additionally, it offers options to access space and extractor mini-games.
 */
package com.csse3200.game.components.player;

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
public class DeathScreenDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(DeathScreenDisplay.class);
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
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset("images/deathscreen.png", Texture.class));
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);

        // Create button for exit options
        TextButton exitBtn = new TextButton("Exit", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exitBtn");
                    }
                });

        // Arrange UI elements in a table layout
        table.add(titleImage);
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
