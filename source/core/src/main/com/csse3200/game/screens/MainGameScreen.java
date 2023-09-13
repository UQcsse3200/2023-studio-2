package com.csse3200.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.ProximityControllerComponent;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.PlayerFactory;
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
import com.csse3200.game.ui.ItemBox;
import com.csse3200.game.ui.TitleBox;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.csse3200.game.ui.UIComponent.skin;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private TitleBox titleBox;
private static boolean alive = true;
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png",
          "images/structure-icons/wall.png",
          "images/structure-icons/turret.png",
          "images/structures/heal.png"}; //TODO: Refactor this to be dynamic? Like game area
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  private Entity player;

  private GameArea gameArea;
  private TerrainFactory terrainFactory;

  private ItemBox itemBox;
  Entity currentExtractor = null;

  public MainGameScreen(GdxGame game) {
    this.game = game;
    this.alive = true;

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService(InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD)));
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    ServiceLocator.registerGameStateObserverService(new GameStateObserver());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    logger.debug("Initialising main game screen entities");
    terrainFactory = new TerrainFactory(renderer.getCamera());

    gameArea = new MapGameArea("configs/earthLevelConfig.json", terrainFactory, game);
    gameArea.create();
    player = ((MapGameArea) gameArea).getPlayer();
    player.getEvents().addListener("death", this::initiateDeathScreen);
    titleBox = new TitleBox(game, "Title", skin);

    itemBox = new ItemBox(((EarthGameArea) gameArea).getExtractorIcon(), renderer);
    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(ServiceLocator.getInputService());
    inputMultiplexer.addProcessor(new InputComponent() {
      @Override
      public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.O) {
          itemBox.triggerShow();
        }
        return false;
      }
      @Override
      public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        currentExtractor = null;
        return super.touchUp(screenX, screenY, pointer, button);
      }
    });
    Gdx.input.setInputProcessor(inputMultiplexer);

  }

  /**
   * When player dies, the PlayerDeath screen is launched.
   */
  public void initiateDeathScreen() {
    alive = false;
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    followPlayer();
    renderer.render();

    itemBox.render();
    if(itemBox.itemContainMouse() && currentExtractor == null){
      currentExtractor = ((EarthGameArea) gameArea).getExtractor();
    }
    if(currentExtractor != null){
      Vector2 mousePos = renderer.getCamera().getWorldPositionFromScreen(new Vector2(Gdx.input.getX() - 200,Gdx.input.getY() + 200));
      currentExtractor.setPosition(mousePos.x,mousePos.y);
    }
    if (alive == false) {
      logger.info("Launching player death screen");
      game.setScreen(GdxGame.ScreenType.PLAYER_DEATH);
    }

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
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
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
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);

  }

  private void followPlayer() {
    float playerX = player.getPosition().x;
    float playerY = player.getPosition().y;

    // Calculate half of the camera's viewport dimensions
    float halfViewportWidth = renderer.getCamera().getCamera().viewportWidth * 0.5f;
    float halfViewportHeight = renderer.getCamera().getCamera().viewportHeight * 0.5f;

    // Define the minimum and maximum allowed camera positions based on map boundaries
    float minX = halfViewportWidth;
    float maxX = 60 * 0.5f - halfViewportWidth;
    float minY = halfViewportHeight;
    float maxY = 60 * 0.5f - halfViewportHeight;

    // Calculate the camera's new X and Y positions within map boundaries
    float cameraX = Math.min(maxX, Math.max(minX, playerX));
    float cameraY = Math.min(maxY, Math.max(minY, playerY));

    //Set new position
    renderer.getCamera().getEntity().setPosition(cameraX, cameraY);
  }

}
