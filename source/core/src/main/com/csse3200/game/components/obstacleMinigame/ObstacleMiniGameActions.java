package com.csse3200.game.components.obstacleMinigame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
