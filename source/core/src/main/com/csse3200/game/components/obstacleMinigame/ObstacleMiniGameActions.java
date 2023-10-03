package com.csse3200.game.components.obstacleMinigame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.screens.SpaceMapScreen;

public class ObstacleMiniGameActions extends MainGameActions {
    private static final Logger logger = LoggerFactory.getLogger(ObstacleMiniGameActions.class);
    private final GdxGame game;
    private final Stage stage;
    private final SpaceMapScreen screen;

    public ObstacleMiniGameActions(GdxGame game, Stage stage, SpaceMapScreen screen) {
        super(game);
        this.game = game;
        this.stage = stage;
        this.screen = screen;
    }


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
