package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.InitialSequence.InitialScreenDisplay;
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

/**
 * The initial screen of the game, responsible for loading assets and creating UI elements.
 */
public class InitialScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreen.class);

    /**
     * An array of paths to image textures needed for this screen.
     */
    private static final String[] introScreenAssets = {"images/InitialScreenBG.png", "images/InitialScreenImage.png"};
    private final GdxGame game;
    private final Renderer renderer;

    /**
     * Creates a new instance of the InitialScreen.
     *
     * @param game The main game instance.
     */
    public InitialScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising controls screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());

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
        Stage stage = ServiceLocator.getRenderService().getStage();
        Viewport viewport = stage.getViewport();
        viewport.update(width, height, true);
    }


    @Override
    public void dispose() {
        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        unloadAssets();
        ServiceLocator.clear();
    }

    /**
     * Load all the image textures required for this screen into memory.
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(introScreenAssets);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Remove all the loaded image textures from the ResourceService, and thus game memory.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(introScreenAssets);
    }

    /**
     * Creates the intro screen's UI, including components for rendering UI elements to the screen
     * and capturing and handling UI input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.setViewport(viewport);

        Entity ui = new Entity();
        ui.addComponent(new InitialScreenDisplay(game))
                .addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}