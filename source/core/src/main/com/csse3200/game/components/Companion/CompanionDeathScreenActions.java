package com.csse3200.game.components.Companion;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanionDeathScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionDeathScreenActions.class);

    public static GdxGame game;

    public CompanionDeathScreenActions(GdxGame game) {
        this.game = game;
    }
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("respawn", this::onRespawn);
    }
    private void onExit() {
        logger.info("Launching main menu screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
    private void onRespawn(){
        logger.info("Relaunching main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}
