package com.csse3200.game.components.controls;



import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlsScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreenActions.class);
    private GdxGame game;
    public ControlsScreenActions(GdxGame game) {
        this.game = game;
    }
    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("w", this::onW);
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

    private void onExit() {
        logger.info("Game returned to the main menu ");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }
}
