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

        // Display game title image
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset("images/deathscreen.png", Texture.class));
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);

        // Create buttons for restart and exit options
        TextButton exitBtn = new TextButton("Exit to Main Menu", skin);
        TextButton restartBtn = new TextButton("Restart Level", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        restartBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Restart button clicked");
                        entity.getEvents().trigger("restart");
                    }
                });

        // Arrange UI elements in a table layout
        tableImage.add(titleImage);
        tableButtons.add(restartBtn).padBottom(50f);
        tableButtons.row();
        tableButtons.add(exitBtn).padBottom(300f);

        stage.addActor(titleImage);
        stage.addActor(tableImage);
        stage.addActor(tableButtons);
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
