package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.ReturnToPlanetDisplay;
import com.csse3200.game.components.obstacleMinigame.ObstacleMiniGameActions;
import com.csse3200.game.components.ships.DistanceDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.input.InputService;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.areas.map_config.MiniGameAssetsConfig;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The game screen containing the main game.
 * Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class SpaceMapScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SpaceMapScreen.class);
    private static Vector2 CAMERA_POSITION = new Vector2(15f, 10f);
    private Entity ship;
    private Entity goal;
    private final GdxGame game;
    private Renderer renderer;
    private PhysicsEngine physicsEngine;
    private Label distanceLabel;
    private DistanceDisplay distanceDisplay;
    private MiniGameAssetsConfig assets;
    private float distance;

    //Camera variables
    private float maxX;
    private float maxY;
    private float cameraX;
    private float cameraY;


    /**
     * Method for creating the space map screen for the obstacle minigame
     * @param game Obstacle minigame
     */
    public SpaceMapScreen(GdxGame game) {
        this.game = game;
        this.assets = FileLoader.readClass(MiniGameAssetsConfig.class, "levels/obstacleGameAssets.json");
        this.loadServices();
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
        this.ship = ((SpaceGameArea) spaceGameArea).getShip();
        this.goal = ((SpaceGameArea) spaceGameArea).getGoal();
        distanceDisplay = new DistanceDisplay();
        distanceDisplay.create();
        distanceDisplay.updateDistanceUI(0);
    }

    /**
     * Method for rendering the entities in the obstacle minigame
     * alongside updating the distance between two entities
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        followShip();
        // Calculate the distance between the ship and the goal
        distance = SpaceGameArea.calculateDistance(ship, goal);
        distanceDisplay.updateDistanceUI(distance);
        exitonc(distance);
        renderer.render();
    }

    /**
     * Register and load all required services
     */
    public void loadServices() {
        //someone forgot to add the services
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerInputService(new InputService(InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD)));
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();

        ServiceLocator.registerGameStateObserverService(new GameStateObserver());

    }
    /**
     * Method for exiting from the obstacle minigame
     * to the main screen of the game
     * @param d Distance of the spaceship from the exit goal
     */
    public void exitonc(float d)
    {
        if(d < 1.0) {
            this.dispose();
            //this.unloadAssets();
            //Until main map lag bug is solved, this will simply exit Game
            new PlanetTravel(game).beginInstantTravel();
        }
    }

    /**
     * Method for resizing the rendering of the entities
     * @param width Width of the render
     * @param height Height of the render
     */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    /**
     * Method for disposing and unloading
     * all assets onto the main screen
     */
    @Override
    public void dispose() {
        logger.debug("Disposing main game screen");

        renderer.dispose();
        unloadAssets();
        //ServiceLocator.clear();
    }

    /**
     * Loads all assets
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (assets != null) {
            assets.load(resourceService);
        }
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unloads all assets
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (assets != null) {
            assets.unload(resourceService);
        }
        //resourceService.clearAllAssets();
    }

    /**
     * Creates the main game's ui including components for
     * rendering ui elements to the screen and
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
                .addComponent(new ObstacleMiniGameActions(this.game,stage, this))
                .addComponent(new ReturnToPlanetDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().register(ui);

    }

    /**
     * Method for making ship follow the player while making
     * sure that the camera doesn't go beyond the boundaries of the map
     */
    private void followShip() {
        maxX = 59 * 1f - renderer.getCamera().getCamera().viewportWidth * 0.5f;
        maxY = 29 * 1f - renderer.getCamera().getCamera().viewportHeight * 0.5f;
        cameraX = Math.min(maxX, Math.max(renderer.getCamera().getCamera().viewportWidth * 0.5f, ship.getPosition().x));
        cameraY = Math.min(maxY, Math.max(renderer.getCamera().getCamera().viewportHeight * 0.5f, ship.getPosition().y));
        renderer.getCamera().getEntity().setPosition(cameraX, cameraY);
    }
}