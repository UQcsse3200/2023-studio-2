package com.csse3200.game.components.controls;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
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
import com.csse3200.game.services.TimingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.ui.UIComponent.skin;

/**
 * The game screen containing the main menu.
 */
public class ControlsAnimation extends ScreenAdapter {
    public static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.screens.ControlsScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    public static final int frameCount = 105;
    private static final String[] ControlTextures = {"images/escape-earth2.png"};
    private static final String[] ConTextures = {"images/escape-earth2.png"};
    public static String[] transitionTexturess = new String[frameCount];
    private static final String animationPrefix = "images/main_menu_video/menu_animations";

    public ControlsAnimation(GdxGame game) {
        this.game = game;

        logger.debug("Initialising main menu screen services");
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeService(new TimingService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        ServiceLocator.getTimingService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }


    @Override
    public void dispose() {
        logger.debug("Disposing main menu screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(ControlTextures);
        loadFrames();
        ServiceLocator.getResourceService().loadAll();
    }
    private void loadFrames() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();

        for (int i = 0; i < frameCount; i++) {
            transitionTexturess[i] = animationPrefix + i + ".png";
        }
        resourceService.loadTextures(ControlTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(transitionTexturess);
    }

    /**
     * Creates the main menu's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new MainMenuDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new MainMenuActions(game, stage, skin));
        ServiceLocator.getEntityService().register(ui);
    }
}