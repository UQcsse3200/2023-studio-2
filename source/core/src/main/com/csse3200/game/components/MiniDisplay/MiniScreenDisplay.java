package com.csse3200.game.components.MiniDisplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MiniScreenDisplay class represents a user interface component used in the initial sequence of the game.
 * It displays a background image and a button to launch the game mission.
 */
public class MiniScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MiniScreenDisplay.class);
    private final GdxGame game;
    private Image background;
    private Table rootTable;

    /**
     * Constructs a MiniScreenDisplay instance.
     *
     * @param game The GdxGame instance that manages the game.
     */
    public MiniScreenDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addUIElements();
    }

    public void addUIElements() {
        // Load and set the background image
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/RELAUNCH MISSION-2.png", Texture.class));
        background.setPosition(0, 0);

        // Scale the height of the background to maintain the original aspect ratio of the image
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        // Create a button to launch the game mission
        TextButton LaunchMissionButton = new TextButton("Launch Mission", skin);

        LaunchMissionButton.addListener(new ChangeListener() {

            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("LaunchMission button clicked");
                startGame();
            }
        });

        // Create a root table to arrange UI elements
        rootTable = new Table();
        rootTable.setFillParent(true);

        rootTable.row();

        rootTable.add(LaunchMissionButton).expandX().center().padTop(250f);

        // Add UI elements to the stage
        stage.addActor(background);
        stage.addActor(rootTable);
    }

    public void startGame() {
        game.setScreen(ScreenType.SPACE_MAP);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    @Override
    public void update() {

        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        background.clear();
        super.dispose();
    }

    public void setSkin() {
    }
}
