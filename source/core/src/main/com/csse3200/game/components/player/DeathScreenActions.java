package com.csse3200.game.components.player;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class DeathScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(DeathScreenActions.class);

    public static GdxGame game;

    public DeathScreenActions(GdxGame game) { // Modify the constructor
        this.game = game;
    }


    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("restart", this::onRestart);
    }
    /**
     * Exits to Main Menu game.
     */
    private void onExit() {
        logger.info("Launching main menu screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }


    private void onRestart() {
        logger.info("Relaunching main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}