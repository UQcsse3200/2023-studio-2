package com.csse3200.game.ui.terminal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.MainAlertBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.ui.UIComponent.skin;

public class MainGameActionss extends MainGameActions {
    private static final Logger logger = LoggerFactory.getLogger(MainGameActionss.class);
    private GdxGame game;
    private Stage stage;

    public MainGameActionss(GdxGame game, Stage stage) {
        super(game);
        this.game = game;
        this.stage = stage;
    }

    /**
     * Swaps to the Main Menu screen.
     */
    protected void onReturnPlanet() {
        logger.info("Exiting main game screen");
        MainAlertBox mainAlertBox = new MainAlertBox(game, "Return to planet", skin, "Game Over");
        mainAlertBox.showDialog(stage,()-> game.setScreen((PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet")));
    }
}
