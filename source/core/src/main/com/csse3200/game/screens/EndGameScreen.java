package com.csse3200.game.screens;


import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.EndGame.EndGameActions;
import com.csse3200.game.components.EndGame.EndGameDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndGameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(StoryScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private static final String[] storyTextures = {
            "images/GameOver.png"
    };
    public EndGameScreen(GdxGame game) {
        this.game = game;

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        renderer = RenderFactory.createRenderer();

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
        logger.trace("Resized renderer: ({} x {})", width, height);
    }
    @Override
    public void dispose() {

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(storyTextures);
        ServiceLocator.getResourceService().loadAll();
    }
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(storyTextures);
    }
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        ui.addComponent(new EndGameDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new EndGameActions(game));;
        ServiceLocator.getEntityService().register(ui);
    }
}
