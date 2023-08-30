package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.EarthGameArea;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.maingame.MainGameActions;
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
import com.csse3200.game.ui.TitleBox;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;  //additional imports from here
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import static com.csse3200.game.ui.UIComponent.skin;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private TitleBox titleBox;
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png"};
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  private Entity player;
  private ShapeRenderer shapeRenderer;
  private Texture elixirIconTexture;
  private TextureRegion elixirIconRegion;
  private SpriteBatch spriteBatch;
  private int playerResources = 0; // Initial value of resources
  private int maxResources = 100; // Adjustable

  public MainGameScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
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
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    //ForestGameArea forestGameArea = new ForestGameArea(terrainFactory, game);
    //forestGameArea.create();
    EarthGameArea earthGameArea = new EarthGameArea(terrainFactory, game);
    earthGameArea.create();
    player = earthGameArea.getPlayer();
    titleBox = new TitleBox(game, "Title", skin);


    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setAutoShapeType(true);

    elixirIconTexture = new Texture("images/elixir.png");
    elixirIconRegion = new TextureRegion(elixirIconTexture);

    spriteBatch = new SpriteBatch();

  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    renderer.render();

    playerResources = 50;

    //Added code by Abhijith for resource bar
    shapeRenderer.begin();

    shapeRenderer.set(ShapeType.Filled);
    shapeRenderer.setColor(Color.WHITE);
    shapeRenderer.rect(10, Gdx.graphics.getHeight() - 140, 32, 32); // Keep the elixir icon at the same position

    float progressBarWidth = 320 * ((float) playerResources / maxResources); // Reduced the width by 20%
    progressBarWidth = Math.min(progressBarWidth, 320); // Clamp the width

    shapeRenderer.setColor(Color.GREEN);
    shapeRenderer.set(ShapeType.Filled);
    shapeRenderer.rect(50, Gdx.graphics.getHeight() - 140, 320, 16); // Raised the position by 10 units

    shapeRenderer.setColor(Color.PURPLE);
    shapeRenderer.set(ShapeType.Filled);
    shapeRenderer.rect(50, Gdx.graphics.getHeight() - 140, progressBarWidth, 16); // Raised the position by 10 units

    float radius = 8; // Reduced the radius by 20%
    shapeRenderer.setColor(Color.PURPLE);
    shapeRenderer.set(ShapeType.Filled);
    //shapeRenderer.arc(50 + progressBarWidth - radius, Gdx.graphics.getHeight() - 132 + radius, radius, -90, 180, 20);

    shapeRenderer.end();

    spriteBatch.begin();
    spriteBatch.draw(elixirIconRegion, 20, Gdx.graphics.getHeight() - 140, 24, 24); // Keep the elixir icon at the same position
    spriteBatch.end();
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
