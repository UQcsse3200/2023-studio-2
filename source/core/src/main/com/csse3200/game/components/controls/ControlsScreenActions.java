package com.csse3200.game.components.controls;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component that defines actions and event listeners for the Controls Screen.
 */
public class ControlsScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreenActions.class);
    private GdxGame game;

    /**
     * Creates a new instance of ControlsScreenActions.
     *
     * @param game The main game instance.
     */
    public ControlsScreenActions(GdxGame game) {
        this.game = game;
    }

    /**
     * Called when this component is created. Registers event listeners for input actions.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);     entity.getEvents().addListener("w", this::onW);
        entity.getEvents().addListener("a", this::onA);
        entity.getEvents().addListener("s", this::onS);
        entity.getEvents().addListener("d", this::onD);

    }


    private void onW() {
        logger.info("W clicked");

    }
    private void onA() {
        logger.info("A clicked");

    }
    private void onS() {
        logger.info("S clicked");

    }
    private void onD() {
        logger.info("D clicked");

    }


    /**
     * Action to handle the 'exit' event, returning the game to the main menu.
     */
     void onExit() {
        logger.info("Game returned to the main menu");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }

    public void getLogger() {
    }
}