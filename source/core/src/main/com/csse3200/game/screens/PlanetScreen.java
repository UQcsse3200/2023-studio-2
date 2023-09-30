package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.ProximityControllerComponent;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A screen that represents a single planet of the game with its corresponding game area/s.
 * Determines which game area to generate and the next planet in the chain.
 */
public class PlanetScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlanetScreen.class);
    private final GdxGame game;

    public final String name;
    private String nextPlanetName;

    private Entity player;

    private String currentAreaName = "primary";
    private final Map<String, GameArea> allGameAreas = new HashMap<>();

    /** Starting position of the camera */
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

    /** Service Instances */
    private Renderer renderer;
    private PhysicsEngine physicsEngine;

    /** file paths of textures for screen to load. */
    private static final String[] planetTextures = {
            "images/player/heart.png",
            "images/structure-icons/gate.png",
            "images/structure-icons/wall.png",
            "images/structure-icons/stone_wall.png",
            "images/structure-icons/turret.png",
            "images/structures/closed_gate.png",
            "images/structures/open_gate.png",
            "images/structures/dirt_wall.png",
            "images/structures/stone_wall.png",
            "images/structures/TurretOne.png",
            "images/structures/TurretTwo.png",
            "images/structures/heal_icon.png"
    };

    /**
     * Construct the PlanetScreen instance for the first planet (Earth).
     *
     * @param game  The current game instance to display screen on.
     */
    public PlanetScreen(GdxGame game) {
        this(game, "Earth");
    }

    /**
     * Construct the PlanetScreen instance for the planet of given name.
     *
     * @param game  The current game instance to display screen on.
     * @param name  The name of the planet to create a screen for.
     */
    public PlanetScreen(GdxGame game, String name) {
        this.game = game;
        this.name = name;
    }

    /**
     * Create the screen services and game areas on show.
     */
    @Override
    public void show() {
        registerServices();

        loadAssets();
        createUI();
        generateGameAreas();
        allGameAreas.get(currentAreaName).create();

        logger.debug((String.format("Initialising %s screen entities", this.name)));
        this.player = allGameAreas.get(currentAreaName).getPlayer();
    }

    /**
     * Get the next planet in the sequence after the current planet.
     *
     * @return  The PlanetScreen instance for the next planet.
     */
    public PlanetScreen getNextPlanet() {
        return new PlanetScreen(this.game, this.nextPlanetName);
    }

    /**
     * Sets the current game area the player is on.
     *
     * @param name  The name of the game area.
     */
    public void setCurrentArea(String name) {
        this.allGameAreas.get(currentAreaName).dispose();
        this.currentAreaName = name;
        this.allGameAreas.get(currentAreaName).create();
    }

    /**
     * Generates all the appropriate game areas for the current planet based on its name.
     */
    private void generateGameAreas() {
        if ("Earth".equals(name)) {
            this.nextPlanetName = "Verdant Oasis";

;            generateGameArea("primary", "levels/earth/main-area");
        } else if ("Verdant Oasis".equals(name)){
            this.nextPlanetName = "Glacial Desolation";
            generateGameArea("primary", "levels/lush/main-area");
        } else if ("Glacial Desolation".equals(name)){
            this.nextPlanetName = "Infernal Challenge";
            generateGameArea("primary", "levels/frozen/main-area");
        } else if ("Infernal Challenge".equals(name)){
            generateGameArea("primary", "levels/lush/main-area");
        } else {
            generateGameArea("primary", "levels/earth/main-area");
        }
    }

    /**
     * Initialises a new game area with a given name based upon the config file.
     *
     * @param name  The name of the game area to create.
     * @param configPath    The configPath to load.
     */
    private void generateGameArea(String name, String configPath) {
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        GameArea gameArea = new MapGameArea(configPath, terrainFactory, game, game.getPlayerLives());
        this.allGameAreas.put(name, gameArea);
        ServiceLocator.registerGameArea(gameArea);
    }

    /**
     * Register all the required screen services.
     */
    private void registerServices() {
        logger.debug(String.format("Initialising %s screen services", this.name));

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerInputService(new InputService(InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD)));
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();

        ServiceLocator.registerGameStateObserverService(new GameStateObserver());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());
    }

    @Override
    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        followPlayer();
        renderer.render();

        ProximityControllerComponent proximityController = player.getComponent(ProximityControllerComponent.class);
        if (proximityController != null) {
            proximityController.checkAllEntitiesProximity();   //checks whether the player is near an intractable entity to show the prompt
        }
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

    /**
     * Do not dispose of all services and renderers on screen switch. Preserve state
     */
    @Override
    public void dispose() {
        this.clear();
    }

    /**
     * Dispose of the entire game screen.
     */
    public void clear() {
        logger.debug(String.format("Disposing %s screen", this.name));

        for (GameArea area : allGameAreas.values()) {
            area.dispose();
        }

        renderer.dispose();
        unloadAssets();
    }

    /**
     * Load in all the assests for the screen.
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(planetTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unload all the screen assets.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(planetTextures);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating UI");
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

    /**
     * Move the camera to follow the player around at screen centre.
     */
    private void followPlayer() {
        // Calculate half of the camera's viewport dimensions
        float halfViewportWidth = renderer.getCamera().getCamera().viewportWidth * 0.5f;
        float halfViewportHeight = renderer.getCamera().getCamera().viewportHeight * 0.5f;

        // Define the minimum and maximum allowed camera positions based on map boundaries
        float minX = halfViewportWidth;
        float maxX = 90 * 0.5f - halfViewportWidth;
        float minY = halfViewportHeight;
        float maxY = 90 * 0.5f - halfViewportHeight;

        // Calculate the camera's new X and Y positions within map boundaries
        float cameraX = Math.min(maxX, Math.max(minX, player.getPosition().x));
        float cameraY = Math.min(maxY, Math.max(minY, player.getPosition().y));

        //Set new position
        renderer.getCamera().getEntity().setPosition(cameraX, cameraY);
    }

}