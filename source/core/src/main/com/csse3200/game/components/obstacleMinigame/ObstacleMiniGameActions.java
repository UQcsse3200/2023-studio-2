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
    private final Stage stage;
    private final SpaceMapScreen screen;

    /**
     * Creates a ObstacleMiniGameActions that handles events that exit SpaceGameScreen
     * @param game Current Game
     * @param stage Current Stage
     * @param screen Current Screen to be handled
     */
    public ObstacleMiniGameActions(GdxGame game, Stage stage, SpaceMapScreen screen) {
        super(game);
        this.game = game;
        this.stage = stage;
        this.screen = screen;
    }


    /**
     * Creates listeners for specific events
     */
    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);

        //Temporary.
        //entity.getEvents().addListener("returnPlanet", this::onReturnPlanet);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
