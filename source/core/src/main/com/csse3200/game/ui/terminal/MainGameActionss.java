package com.csse3200.game.ui.terminal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.ui.MainAlertBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.ui.UIComponent.skin;

public class MainGameActionss extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainGameActionss.class);
    private GdxGame game;
    private Stage stage;

    public MainGameActionss(GdxGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting main game screen");
        MainAlertBox mainAlertBox = new MainAlertBox(game, "Exit game", skin, "Game Over");
        mainAlertBox.showDialog(stage,()-> {
            game.setScreen(GdxGame.ScreenType.MAIN_MENU);
        });

    }
}
