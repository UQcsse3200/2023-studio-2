/**
 * DeathScreenDisplay is a UI component responsible for displaying the death screen of the game.
 * It provides buttons for restarting the game and exiting the game.
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
import com.csse3200.game.areas.mapConfig.GameAreaConfig;
import com.csse3200.game.entities.configs.SoundsConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The UI component responsible for rendering and managing the death screen interface.
 * It provides options for restarting and exiting the game.
 */
public class DeathScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(DeathScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table tableImage;
    private Table tableButtons;
    private final int lives;
    private static final String[] deathScreenTextures = {"images/deathscreens/deathscreen_0.jpg", "images/deathscreens/deathscreen_1.jpg", "images/deathscreens/deathscreen_2.jpg", "images/deathscreens/deathscreen_3.jpg"};

    public DeathScreenDisplay(int lives) {
        this.lives = lives;
    }
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds UI elements such as buttons and title to the death screen display.
     */
    private void addActors() {
        tableImage = new Table();
        tableButtons = new Table();
        tableButtons.bottom();
        tableImage.setFillParent(true);
        tableButtons.setFillParent(true);

        // Display death screen image
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset(deathScreenTextures[lives], Texture.class));

        // Resize to fit window
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);

        // Create buttons for respawn and exit options
        TextButton exitBtn = new TextButton("Exit to Main Menu", skin);
        TextButton respawnBtn = new TextButton("Respawn", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        respawnBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Respawn button clicked");
                    entity.getEvents().trigger("respawn");
                     }
                });

        // Arrange UI elements in a table layout
        tableImage.add(titleImage);
        if (lives > 0) {
//          Play the respawn sound when the player live still remaining to respawn
            entity.getEvents().trigger("playSound", "respawn");

            tableButtons.add(respawnBtn).padBottom(Gdx.graphics.getHeight() * 0.05f);
            tableButtons.row();
            tableButtons.add(exitBtn).padBottom(Gdx.graphics.getHeight() * 0.2f);
        } else {
//          Play the dead sound when the player live not remaining to respawn
            entity.getEvents().trigger("playSound", "dead");

            tableButtons.row();
            tableButtons.add(exitBtn).padBottom(Gdx.graphics.getHeight() * 0.3f);
        }
        stage.addActor(titleImage);
        stage.addActor(tableImage);
        stage.addActor(tableButtons);
    }

    public void resize() {
        tableImage.clear();
        tableButtons.clear();
        addActors();
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
        tableImage.clear();
        tableButtons.clear();
        super.dispose();
    }
}
