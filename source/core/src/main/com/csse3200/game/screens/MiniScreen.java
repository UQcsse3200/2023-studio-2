package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.MiniDisplay.MiniScreenDisplay;
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
 * The MiniScreen class represents a screen in the game that displays a mini-screen with initial content.
 * It initializes game services, loads assets, and creates the user interface for the mini-screen.
 */
public class MiniScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SpaceMapScreen.class);

    /**
     * An array of paths to image textures needed for this screen.
     */
    private static final String[] introScreenAssets = {"images/RELAUNCH MISSION-2.png", "images/RELAUNCH MISSION-2.png"};
    private final GdxGame game;
    private final Renderer renderer;

    /**
     * Constructs a MiniScreen instance.
     *
     * @param game The GdxGame instance that manages the game.
     */
    public MiniScreen(GdxGame game) {
        this.game = game;


        logger.debug("Initializing control screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());
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
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new MiniScreenDisplay(game))
                .addComponent(new InputDecorator(stage, 12));
        ServiceLocator.getEntityService().register(ui);
    }
}
