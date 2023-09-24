package com.csse3200.game.components.obstacleMinigame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.SpaceMiniTransition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.ui.UIComponent.skin;

public class ObstacleMiniGameActions extends MainGameActions {
    private static final Logger logger = LoggerFactory.getLogger(ObstacleMiniGameActions.class);
    private final GdxGame game;
    private final Stage stage;

    public ObstacleMiniGameActions(GdxGame game, Stage stage) {
        super(game);
        this.game = game;
        this.stage = stage;
    }


    @Override
    public void create() {
        entity.getEvents().addListener("returnPlanet", this::onReturnPlanet);
    }
    /**
     * Swaps to the Main Menu screen.
     */

    /**
     * Called when exit screen is triggered, display a dialog before switching the screen
     */
    protected void onReturnPlanet() {
        logger.info("Exiting main game screen");


        SpaceMiniTransition mainAlertBox = new SpaceMiniTransition(game, "Return to planet", skin, "Game Over");
        mainAlertBox.showDialog(stage,()->
                game.setScreen((PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet")));
    }


}
