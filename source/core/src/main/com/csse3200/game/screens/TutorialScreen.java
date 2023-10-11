package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.GameArea;
//import com.csse3200.game.areas.TutorialDialogue;
import com.csse3200.game.areas.TutorialGameArea;
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
import com.csse3200.game.ui.TitleBox;
import com.csse3200.game.ui.TutorialDialogue;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.csse3200.game.ui.UIComponent.skin;

public class TutorialScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlanetScreen.class);
    private final GdxGame game;

    private final String name;

private Stage stage;
    private Entity player;

    private String currentAreaName = "primary";
    private final Map<String, GameArea> allGameAreas = new HashMap<>();

    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

    private Renderer renderer;
    private PhysicsEngine physicsEngine;

    /** file paths of textures for screen to load. */
    private static final String[] planetTextures = {
            "images/heart.png",
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

    public TutorialScreen(GdxGame game, String name) {
        this.game = game;
        this.name = name;
    }

    @Override
    public void show() {
        registerServices();

        loadAssets();
        createUI();
        generateGameAreas();
        allGameAreas.get(currentAreaName).create();

        logger.debug((String.format("Initialising %s screen entities", this.name)));
        showTutorialDialogueBox();
        this.player = allGameAreas.get(currentAreaName).getPlayer();
    }
    private void showTutorialDialogueBox() {
        // Create and display the TitleBox
        TutorialDialogue tutorialDialogue = new TutorialDialogue(game, "Tutorial", "Hi! This is the tutorial of the game", skin);
        // Adjust title and skin as needed
        tutorialDialogue.showDialog(ServiceLocator.getRenderService().getStage());

        // Pause the game while the TitleBox is displayed
        Gdx.graphics.setContinuousRendering(true);
    }

    private void generateGameAreas() {
        generateGameArea("primary", "levels\\tutorial\\main-area");

    }
    private TutorialGameArea generateGameArea(String name, String configPath) {
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        TutorialGameArea tutorialGameArea = new TutorialGameArea(terrainFactory, game);
        this.allGameAreas.put(name, tutorialGameArea);
        return tutorialGameArea;
    }

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

    @Override
    public void dispose() {
        this.clear();
    }

    /**
     * Disposes of the game screen.
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
     * Loads the assets for the game screen.
     */

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(planetTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unloads the assets for the game screen.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(planetTextures);
    }

    /**
     * Creates the UI entities for the game screen.
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
