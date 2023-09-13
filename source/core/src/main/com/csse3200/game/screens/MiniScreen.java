package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.InitialSequence.MiniScreenDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EndGame.class);

    /**
     * An array of paths to image textures needed for this screen
     */
    private static final String[] introScreenAssets = {"images/start.png", "images/start.png"};
    private final GdxGame game;
    private final Renderer renderer;

    public MiniScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising controls screen services");

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);

        loadAssets();
        createUI();
    }


    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();

        unloadAssets();
    }


    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(introScreenAssets);
        ServiceLocator.getResourceService().loadAll();
    }


    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(introScreenAssets);
    }


    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new MiniScreenDisplay(game))
                .addComponent(new InputDecorator(stage, 12));
        ServiceLocator.getEntityService().register(ui);
    }
}