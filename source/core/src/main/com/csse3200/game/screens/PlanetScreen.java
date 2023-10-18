package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.areas.map_config.AssetsConfig;
import com.csse3200.game.areas.map_config.ConfigLoader;
import com.csse3200.game.areas.map_config.InvalidConfigException;
import com.csse3200.game.areas.map_config.LevelConfig;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.ProximityControllerComponent;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGamePauseDisplay;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.TitleBox;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.utils.LoadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csse3200.game.utils.LoadUtils.*;
import static com.csse3200.game.ui.UIComponent.skin;

/**
 * A screen that represents a single planet of the game with its corresponding game area/s.
 * Determines which game area to generate and the next planet in the chain.
 */
public class PlanetScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlanetScreen.class);
    private final GdxGame game;

    public final String name;
    private String nextPlanetName = null;

    private Entity player;

    private String currentAreaName = DEFAULT_AREA;
    private final Map<String, MapGameArea> allGameAreas = new HashMap<>();

    private LevelConfig levelConfig = null;

    /** Starting position of the camera */
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

    /** Service Instances */
    private Renderer renderer;
    private PhysicsEngine physicsEngine;
    public static boolean titleBoxDisplayed = false;

    /** file paths of textures for screen to load. */
    private AssetsConfig assets;
    private TitleBox titleBox;

    /**
     * Construct the PlanetScreen instance for the planet of given name.
     *
     * @param game  The current game instance to display screen on.
     * @param name  The name of the planet to create a screen for.
     */
    public PlanetScreen(GdxGame game, String name) {
        logger.info(String.format("Creating %s PlanetScreen", name));
        this.game = game;
        this.name = name;
        this.assets = FileLoader.readClass(AssetsConfig.class, "levels/global_assets.json");

        String levelName = LoadUtils.formatName(name);
        try {
            this.levelConfig = ConfigLoader.loadLevel(levelName);
            this.nextPlanetName = this.levelConfig.nextPlanet;
        } catch (InvalidConfigException e) {
            logger.error("FAILED TO LOAD LEVEL DATA FOR " + levelName);
        }
    }

    /**
     * Create the screen services and game areas on show.
     */
    @Override
    public void show() {
        logger.info(String.format("Displaying %s PlanetScreen", name));
        registerServices();

        populateGameState();

        loadAssets();
        createUI();
        generateGameAreas();
        if (!invalidStateKey("gameArea")) {
            this.currentAreaName = (String) ServiceLocator.getGameStateObserverService().getStateData("gameArea");
        } else {
            ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "gameArea", currentAreaName);
        }

        this.allGameAreas.get(currentAreaName).create();
        this.player = allGameAreas.get(currentAreaName).getPlayer();

        showTitleBox();

        ServiceLocator.registerGameArea(this.allGameAreas.get(currentAreaName));
    }

    private void showTitleBox() {
        if (!titleBoxDisplayed) {
            if ("earth".equals(name)) {
                // Create and display the TitleBox
                String[] title = {"", ""};
                String[] description = {"NPC: (Desperately pleading) Please, you have to get me out of here!\n They captured me when I landed on this planet.", "Player: Don't worry, we'll get you out!"};
                String[] window = {"dialogue_1", "dialogue_3",};

                titleBox = new TitleBox(game, title, description, skin, window);
            }
            else if ("blazes_refuge".equals(name)) {
                // Create and display the TitleBox
                String[] title = {"", ""};
                String[] description = {"NPC: (Desperately pleading) Please, you have to get me out of here!\n They captured me when I landed on this planet.", ""};
                String[] window = {"dialogue_1", "dialogue_2"};

                titleBox = new TitleBox(game, title, description, skin, window);
            }
            else if ("flora_haven".equals(name)) {
                // Create and display the TitleBox
                String[] title = {"", ""};
                String[] description = {"NPC: (Desperately pleading) Please, you have to get me out of here!\n They captured me when I landed on this planet.", ""};
                String[] window = {"dialogue_1", "dialogue_2"};

                titleBox = new TitleBox(game, title, description, skin, window);
            }
            else if ("cryoheim".equals(name)) {
                // Create and display the TitleBox
                String[] title = {"", ""};
                String[] description = {"NPC: (Desperately pleading) Please, you have to get me out of here!\n They captured me when I landed on this planet.", ""};
                String[] window = {"dialogue_1", "dialogue_2"};

                titleBox = new TitleBox(game, title, description, skin, window);
            }
                // Adjust title and skin as needed
                titleBox.showDialog(ServiceLocator.getRenderService().getStage());
                // Adjust title and skin as needed
                titleBox.showDialog(ServiceLocator.getRenderService().getStage());

            // Pause the game while the TitleBox is displayed
            Gdx.graphics.setContinuousRendering(false);
            titleBoxDisplayed = true;
        }
    }

    /**
     * Get the next planet's name in the sequence after the current planet.
     *
     * @return  The name of the next planet.
     */
    public String getNextPlanetName() {
        return this.nextPlanetName;
    }

    /**
     * Sets the current game area the player is on.
     *
     * @param name  The name of the game area.
     * @return      Whether the new game area was set correctly.
     */
    public boolean setCurrentArea(String name) {
        logger.debug("Changing displayed area to " + name);
        if (this.allGameAreas.containsKey(name)) {
            this.allGameAreas.get(currentAreaName).dispose();
            this.currentAreaName = name;
            this.allGameAreas.get(currentAreaName).create();
            this.player = allGameAreas.get(currentAreaName).getPlayer();
            ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "gameArea", this.currentAreaName);
            return true;
        }
        return false;
    }

    /**
     * Get the currently displayed game area
     * @return  MapGameArea being utilised.
     */
    public MapGameArea getCurrentGameArea(){
        return this.allGameAreas.get(currentAreaName);
    }

    /**
     * Populates the game state with important constants or default values if not set yet.
     */
    private void populateGameState(){
        if (invalidStateKey("player/lives")) ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "lives", "set", 3);
        if (invalidStateKey("nextPlanet")) ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "nextPlanet", getNextPlanetName());

        int maxResource = 1000;
        ServiceLocator.getGameStateObserverService().trigger("resourceMax", Resource.Nebulite.toString(), maxResource);
        ServiceLocator.getGameStateObserverService().trigger("resourceMax", Resource.Durasteel.toString(), maxResource);
        ServiceLocator.getGameStateObserverService().trigger("resourceMax", Resource.Solstite.toString(), maxResource);
        ServiceLocator.getGameStateObserverService().trigger("extractorsMax", Resource.Nebulite.toString(), 4);
        ServiceLocator.getGameStateObserverService().trigger("extractorsMax", Resource.Durasteel.toString(), 4);
        ServiceLocator.getGameStateObserverService().trigger("extractorsMax", Resource.Solstite.toString(), 4);
    }

    private boolean invalidStateKey(String key) {
        return ServiceLocator.getGameStateObserverService().getStateData(key) == null;
    }

    /**
     * Generates all the appropriate game areas for the current planet based on its name.
     */
    private void generateGameAreas() {
        logger.debug((String.format("Creating %s PlanetScreen areas", this.name)));
        String levelName = formatName(name);

        if (this.levelConfig != null) {
            for (String area : this.levelConfig.areaNames) {
                generateGameArea(levelName, area);
            }
        }
    }

    /**
     * Initialises a new game area with a given name based upon the config file.
     *
     * @param levelName  The name of the level to load.
     * @param areaName The nam of the game area to be loaded from the level.
     */
    private void generateGameArea(String levelName, String areaName) {
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        this.allGameAreas.put(areaName, new MapGameArea(levelName, areaName, terrainFactory, game));

        MapGameArea gameArea = new MapGameArea(levelName, areaName, terrainFactory, game);
        this.allGameAreas.put(name, gameArea);
    }

    /**
     * Register all the required screen services.
     */
    private void registerServices() {
        logger.debug(String.format("Initialising %s screen services", this.name));

        ServiceLocator.registerInputService(new InputService(InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD)));
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerTimeSource(new GameTime());
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
        logger.debug(String.format("Disposing %s PlanetScreen", this.name));
        saveGame();
        this.allGameAreas.get(currentAreaName).dispose();
        renderer.dispose();
        unloadAssets();
        ServiceLocator.getGameStateObserverService().trigger("remove", "gameArea");
    }

    private void saveGame() {
        logger.debug(String.format("Saved %s PlanetScreen", name));
        String path = String.format("%s/%s/%s/entities.json", getSavePath(), this.name, this.currentAreaName);
        ServiceLocator.getEntityService().saveCurrentArea(path);

        Map<String, Object> gameStateEntries = new HashMap<>(ServiceLocator.getGameStateObserverService().getFullStateData());
        FileLoader.writeClass(gameStateEntries, joinPath(List.of(getSavePath(), GAMESTATE_FILE)), FileLoader.Location.LOCAL);
    }

    /**
     * Load in all the assests for the screen.
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
     * Unload all the screen assets.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (assets != null) {
            assets.unload(resourceService);
        }
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
                .addComponent(new MainGamePauseDisplay(this.game.getScreenType()))
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