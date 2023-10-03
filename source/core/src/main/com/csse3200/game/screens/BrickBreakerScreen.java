package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.BricKBreakerGameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrickBreakerScreen extends ScreenAdapter {
    private final GdxGame game;
    private final PhysicsEngine physicsEngine;
    private static final String[] background = {"images/deathscreens/deathscreen_0.jpg"};
    public static final Logger logger = LoggerFactory.getLogger(BrickBreakerScreen.class);
    private final Renderer renderer;
    private static final Vector2 CAMERA_POSITION = new Vector2(15f, 10f);
    public BrickBreakerScreen(GdxGame game){
        this.game = game;
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();


        //Vector2 shipPos = ServiceLocator.getEntityService().getEntitiesByComponent(ShipActions.class).get(0).getPosition();
        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());
        loadAssets();
        createUI();
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        BricKBreakerGameArea bricKBreakerGameArea= new BricKBreakerGameArea(terrainFactory);
        bricKBreakerGameArea.create();
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
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing brick breaker minigame screen");

        renderer.dispose();
        unloadAssets();
    }
    /**
     * Loads all assets
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(background);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unloads all assets
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(background);
        resourceService.clearAllAssets();
    }
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().register(ui);
    }
}
