package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.ReturnToPlanetDisplay;
import com.csse3200.game.components.ships.ShipActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.obstacleMinigame.ObstacleMiniGameActions;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.components.ships.DistanceDisplay;



/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class SpaceMapScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SpaceMapScreen.class);

    private static final Vector2 CAMERA_POSITION = new Vector2(15f, 10f);
    private Entity ship;
    private Entity goal;
    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private Label distanceLabel;
    private DistanceDisplay distanceDisplay;


    public SpaceMapScreen(GdxGame game) {
        this.game = game;
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();

        ServiceLocator.registerGameStateObserverService(new GameStateObserver());

        //Vector2 shipPos = ServiceLocator.getEntityService().getEntitiesByComponent(ShipActions.class).get(0).getPosition();
        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        loadAssets();
        createUI();

        logger.debug("Initialising space minigame screen entities");
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        SpaceGameArea spaceGameArea= new SpaceGameArea(terrainFactory);
        spaceGameArea.create();
        ship = ((SpaceGameArea) spaceGameArea).getShip();
        goal = ((SpaceGameArea) spaceGameArea).getGoal();
        distanceDisplay = new DistanceDisplay();
        distanceDisplay.create();
        distanceDisplay.updateDistanceUI(0);
    }

    @Override
    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        followShip();
        // Calculate the distance between the ship and the goal
        float distance = SpaceGameArea.calculateDistance(ship, goal);
        distanceDisplay.updateDistanceUI(distance);
        renderer.render();

    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }


    @Override
    public void dispose() {
        logger.debug("Disposing main game screen");

        renderer.dispose();
        unloadAssets();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new ObstacleMiniGameActions(this.game,stage))
                .addComponent(new ReturnToPlanetDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().register(ui);

    }

    private void followShip() {
        float maxX = 59 * 1f - renderer.getCamera().getCamera().viewportWidth * 0.5f;
        float maxY = 29 * 1f - renderer.getCamera().getCamera().viewportHeight * 0.5f;
        float cameraX = Math.min(maxX, Math.max(renderer.getCamera().getCamera().viewportWidth * 0.5f, ship.getPosition().x));
        float cameraY = Math.min(maxY, Math.max(renderer.getCamera().getCamera().viewportHeight * 0.5f, ship.getPosition().y));
        renderer.getCamera().getEntity().setPosition(cameraX, cameraY);
    }
}