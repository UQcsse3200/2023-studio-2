package com.csse3200.game.components.controls;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component that defines actions and event listeners for the Controls Screen.
 */
public class ControlsScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreenActions.class);
    private final GdxGame game;
    private int lives;

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
        entity.getEvents().addListener("exitToSettings", this::onExitToSettings);
        entity.getEvents().addListener("exitToGame", this::onExit);
        entity.getEvents().addListener("w", this::onW);
        entity.getEvents().addListener("a", this::onA);
        entity.getEvents().addListener("s", this::onS);
        entity.getEvents().addListener("d", this::onD);
        entity.getEvents().addListener("f", this::onF);
        entity.getEvents().addListener("Space", this::onSpace);
        entity.getEvents().addListener("1", this::on1);
        entity.getEvents().addListener("2", this::on2);
        entity.getEvents().addListener("3", this::on3);
        entity.getEvents().addListener("4", this::on4);
    }

    public void onExitToSettings() {
        System.out.println("settings");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
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
    private void onF() {
        logger.info("F clicked");

    }
    private void onSpace() {
        logger.info("Space key clicked");

    }
    private void on1() {
        logger.info("1 key clicked");

    }
    private void on2() {
        logger.info("2 key clicked");

    }
    private void on3() {
        logger.info("3 key clicked");

    }
    private void on4() {
        logger.info("4 key clicked");

    }

    /**
     * Action to handle the 'exit' event, returning to the current game.
     */
     void onExit() {
         logger.info("Relaunching main game screen");
         ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "lives", "set", (int) ServiceLocator.getGameStateObserverService().getStateData("player/lives"));
         new PlanetTravel(game).returnToCurrent();
    }

    public void getLogger() {
    }
}