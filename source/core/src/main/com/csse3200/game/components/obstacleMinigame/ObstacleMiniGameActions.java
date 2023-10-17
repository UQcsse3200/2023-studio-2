package com.csse3200.game.components.obstacleMinigame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.screens.SpaceMapScreen;

/**
 * This class listens to events relevant to the SpaceGameScreen and does something when events is triggered
 *
 * <p>Exit to main menu screen event is Triggered on ReturnToPlanetDisplay</p>
 */
public class ObstacleMiniGameActions extends MainGameActions {
    private static final Logger logger = LoggerFactory.getLogger(ObstacleMiniGameActions.class);
    private final GdxGame game;

    /**
     * Creates a ObstacleMiniGameActions that handles events that exit SpaceGameScreen
     * @param game Current Game
     * @param stage Current Stage
     * @param screen Current Screen to be handled
     */
    public ObstacleMiniGameActions(GdxGame game, Stage stage, SpaceMapScreen screen) {
        super(game);
        this.game = game;
    }


    /**
     * Creates listeners for specific events
     */
    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::exit);
        entity.getEvents().addListener("restart", this::restartGame);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    public void exit() {
        logger.info("Exiting main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Restarts the space obstacle minigame. Currently capable of only generating a new space minigame
     */
    private void restartGame() {
        logger.info("Restart space minigame");
        game.setScreen(GdxGame.ScreenType.SPACE_MAP);
    }
}
