package com.csse3200.game.components.maingame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.windows.PauseWindow;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private GdxGame game;
  private Array<Entity> entityList;
  private static final PlayerConfig config =
          FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Initiates MainGameActions using game parameter.
   * @param game current game initialised.
   */
  public MainGameActions(GdxGame game) {
    this.game = game;
  }

  /**
   * Set the entity to which this component belongs. This is called by the Entity, and should not be
   * set manually.
   *
   * @param entity The entity to which the component is attached.
   */
  public void setEntity(Entity entity) {
    logger.debug("Attaching {} to {}", this, entity);
    this.entity = entity;
  }
  @Override
  public void create() {
    entity.getEvents().addListener("pause", this::onPauseButton);
    entity.getEvents().addListener("exitPressed", this::onExit);
    entity.getEvents().addListener("returnPressed", this::onReturnButton);
    entity.getEvents().addListener("controlsPressed", this::onControlsButton);
    entity.getEvents().addListener("pauseGame", this::paused);
    entity.getEvents().addListener("resumeGame", this::resumed);
  }
  /**
   * Opens pause window.
   */
  private void onPauseButton() {
    if (!isWindowOpen()) {
      logger.info("Opening Pause Menu");
      PauseWindow pauseWindow = PauseWindow.makeNewPauseWindow(entity);
      paused();
      ServiceLocator.getRenderService().getStage().addActor(pauseWindow);
    }
}

  /**
   * Exits to Main Menu screen. Closes pause window.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "lives", "set", config.lives);
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Exits to Controls screen. Closes pause window.
   */
  private void onControlsButton() {
    logger.debug("Control Screen button clicked");
    game.setScreen(GdxGame.ScreenType.CONTROL_SCREEN);
  }

  /**
   * Returns to current game. Closes pause window.
   */
  protected void onReturnButton() {
    logger.info("Returning to current game");
    resumed();
  }

  protected void paused() {
    entityList = ServiceLocator.getEntityService().getEntities();
    for (Entity entity : entityList) {
      if (entity.getId() != getEntity().getId()) {
        entity.setEnabled(false);
      }
    }
    ServiceLocator.getEntityService().getPlayer().getComponent(CombatStatsComponent.class).setImmunity(true);
  }
  protected void resumed() {
    for (Entity entity : entityList) {
      entity.setEnabled(true);
    }
    ServiceLocator.getEntityService().getPlayer().getComponent(CombatStatsComponent.class).setImmunity(false);
  }
  public boolean isWindowOpen() {
    for (Actor actor : ServiceLocator.getRenderService().getStage().getActors()) {
      if (actor instanceof Window && actor.isVisible()) {
        return true;
      }
    }
    return false;
  }
}