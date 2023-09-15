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
    private final GdxGame game;

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
        entity.getEvents().addListener("exit", this::onExit);
    }


    /**
     * Action to handle the 'exit' event, returning the game to the main menu.
     */
    private void onExit() {
        logger.info("Game returned to the main menu");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }
}