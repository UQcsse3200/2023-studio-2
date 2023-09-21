package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.ControlsAnimation.ControlsAnimationActions;
import com.csse3200.game.components.ControlsAnimation.ControlsAnimationDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.ui.UIComponent.skin;

public class ControlsAnimationScreen extends ScreenAdapter {
    public static final Logger logger = LoggerFactory.getLogger(ControlsAnimationScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    public static final int MountedFrames = 41;
    private static final String[] ControlsAnimationTextures = {"images/Controls.png"};
    public static String[] transitionTextures = new String[MountedFrames];
    private static final String animationPrefix = "images/controlsanimation/forward/forward";

    public ControlsAnimationScreen(GdxGame game) {
        this.game = game;
        logger.debug("Initialising controls animation services");
        initialiseServices();
        renderer = RenderFactory.createRenderer();
        loadAssets();
        createUI();
    }

    private void initialiseServices() {
        ServiceLocator.registerInputService(
                new InputService(InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD)));
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerGameStateObserverService(new GameStateObserver());
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
        logger.debug("Dispose controls animation screen");
        renderer.dispose();
        unloadAssets();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(ControlsAnimationTextures);
        loadMountedFrames();
        ServiceLocator.getResourceService().loadAll();
    }

    private void loadMountedFrames() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        for (int i = 0; i < MountedFrames; i++) {
            transitionTextures[i] = animationPrefix + i + ".png";
        }
        resourceService.loadTextures(transitionTextures);
        ServiceLocator.getResourceService().loadAll();
    }


    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(ControlsAnimationTextures);
    }

    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new ControlsAnimationDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new ControlsAnimationActions(game, stage, skin));
        ServiceLocator.getEntityService().register(ui);
    }
}
