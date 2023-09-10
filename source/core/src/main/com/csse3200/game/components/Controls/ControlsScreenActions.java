package com.csse3200.game.components.Controls;



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
    }

    private void onExit() {
        logger.info("Game returned to the main menu ");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }
}
