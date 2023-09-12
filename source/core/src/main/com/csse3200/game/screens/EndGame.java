package com.csse3200.game.screens;


import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndGame extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(StoryScreen.class);

    public EndGame(GdxGame gdxGame) {
    }


    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();

                ui.addComponent(new InputDecorator(stage, 10));

        ServiceLocator.getEntityService().register(ui);
    }
}