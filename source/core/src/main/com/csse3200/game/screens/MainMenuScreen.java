package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.components.mainmenu.MainMenuStarBackground;
import com.csse3200.game.components.spacenavigation.NavigationBackground;
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

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends ScreenAdapter {
  public static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final GdxGame game;
  private final Renderer renderer;
  private static final String[] mainMenuTextures = {"images/menu/main-menu.png"};

  public MainMenuScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising main menu screen services");
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
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main menu screen");

    renderer.dispose();
    unloadAssets();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
  }

  /**
   * Creates the main menu's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();

    Entity ui = new Entity();
    ui.addComponent(new MainMenuDisplay())
            .addComponent(new InputDecorator(stage, 10))
            .addComponent(new MainMenuActions(game, stage, skin));
    ServiceLocator.getEntityService().register(ui);
    stage.addActor(new MainMenuStarBackground());
  }
}