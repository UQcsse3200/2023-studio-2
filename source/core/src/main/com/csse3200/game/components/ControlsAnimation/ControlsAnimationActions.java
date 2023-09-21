package com.csse3200.game.components.ControlsAnimation;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.MainAlert;
import com.csse3200.game.ui.AlertBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlsAnimationActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ControlsAnimationActions.class);

    public static GdxGame game;
    private Stage stage; // Add the stage
    private Skin skin;   // Add the skin

    public ControlsAnimationActions(GdxGame game, Stage stage, Skin skin) { // Modify the constructor
        this.game = game;
        this.stage = stage; // Initialize stage
        this.skin = skin;   // Initialize skin
    }


    @Override
    public void create() {
        entity.getEvents().addListener("settings", this::onSettings);
    }


    /**
     * Swaps to the Controls screen.
     */
    private void onSettings() {
        logger.info("Launching controls screen");
        game.setScreen(GdxGame.ScreenType.CONTROL_SCREEN);
    }
}
