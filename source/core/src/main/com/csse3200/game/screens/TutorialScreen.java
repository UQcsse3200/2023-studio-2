package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.TutorialGameArea;
import com.csse3200.game.areas.GameArea;
import static com.badlogic.gdx.Gdx.app;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.ProximityControllerComponent;
import com.csse3200.game.components.controls.ControlsScreenActions;
import com.csse3200.game.components.controls.ControlsScreenDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGamePauseDisplay;
import com.csse3200.game.components.mainmenu.InsertButtons;
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
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Gdx.app;

public class TutorialScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreen.class);
    private final GdxGame game;

    private final String name;

    private Stage stage;
    private Entity player;

    private final Map<String, GameArea> allGameAreas = new HashMap<>();

    private static final Vector2 CAMERA_POSITION = new Vector2(10f, 7.5f);

    private Renderer renderer;
    private PhysicsEngine physicsEngine;

    TutorialGameArea controlGameArea;

    /**
     * file paths of textures for screen to load.
     */
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
            "images/explosives/landmine.png",
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
        String currentAreaName = "primary";
        allGameAreas.get(currentAreaName).create();

        logger.debug((String.format("Initialising %s screen entities", this.name)));
        //showTutorialDialogueBox();
        this.player = GameArea.getPlayer();
        this.stage=renderer.getStage();

        Texture storyLine = new Texture(Gdx.files.internal("images/controls-images/Controls.png"));
        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
        Image image = new Image(storyBackground);
        image.setHeight((float) Gdx.graphics.getHeight() / 2 - 150);
        image.setWidth(Gdx.graphics.getWidth());
        image.setPosition(0,stage.getHeight()-image.getHeight());
      //  stage.addActor(image);

        InsertButtons bothButtons = new InsertButtons();

        String exitTexture = "images/controls-images/on_exit.png";
        String exitTextureHover = "images/controls-images/on_exit_hover.PNG";
        ImageButton exitBtn;
        exitBtn = bothButtons.draw(exitTexture, exitTextureHover);
        exitBtn.setPosition(stage.getWidth()-300f, stage.getHeight()-1100f);
        exitBtn.setSize(250, 100);

        Entity entity = new Entity();
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                entity.getEvents().trigger("exit");
            }
        });
        entity.getEvents().addListener("exit", this::onExit);
        stage.addActor(exitBtn);

    }

    public void onExit() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    private void generateGameAreas() {
        generateGameArea();
    }

    private void generateGameArea() {
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        controlGameArea = new TutorialGameArea(terrainFactory, game);
        this.allGameAreas.put("primary", controlGameArea);
    }

    private void registerServices() {
        logger.debug(String.format("Initialising %s screen services", this.name));

        ServiceLocator.registerInputService(new InputService());
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

    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        renderer.render();
        followPlayer();
        ProximityControllerComponent proximityController = player.getComponent(ProximityControllerComponent.class);
        if (proximityController != null) {
            proximityController.checkAllEntitiesProximity();   //checks whether the player is near an intractable entity to show the prompt
        }
    }

    @Override
    public void resize(int width, int height) {
        //renderer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-400);
        // Example: Change the camera's viewport to half the size
        renderer.getCamera().resize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,15);
        renderer.getCamera().update();
        renderer.render();



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
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        boolean Isgame = false;
       // ui.addComponent(new ControlsScreenDisplay(game, Isgame));
        System.out.println(game);
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