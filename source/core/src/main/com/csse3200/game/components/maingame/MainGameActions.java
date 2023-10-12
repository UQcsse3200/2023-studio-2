package com.csse3200.game.components.maingame;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.EnvironmentStatsComponent;
import com.csse3200.game.windows.PauseWindow;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.PlanetTravel;
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

  @Override
  public void create() {
    entity.getEvents().addListener("pause", this::onPauseButton);
    entity.getEvents().addListener("returnPlanet", this::onReturnPlanet);
    entity.getEvents().addListener("exitPressed", this::onExit);
    entity.getEvents().addListener("returnPressed", this::onReturnButton);
  }
  /**
   * Opens pause window.
   */
  private void onPauseButton() {
    logger.info("Opening Pause Menu");
    PauseWindow pauseWindow = PauseWindow.makeNewPauseWindow(entity);
    entityList = ServiceLocator.getEntityService().getEntities();
    for (Entity entity : entityList) {
      if (entity.getId() != getEntity().getId()) {
        entity.setEnabled(false);
      }
    }
    ServiceLocator.getEntityService().getPlayer().getComponent(CombatStatsComponent.class).setImmunity(true);
    ServiceLocator.getRenderService().getStage().addActor(pauseWindow);
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
   * Returns to current planet screen.
   */
  protected void onReturnPlanet() {
    logger.info("Exiting to current planet screen");
    ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "lives", "set", config.lives);
    new PlanetTravel(game).returnToCurrent();
    ServiceLocator.getEntityService().getPlayer().getComponent(CombatStatsComponent.class).setImmunity(false);
  }

  /**
   * Returns to current game. Closes pause window.
   */
  protected void onReturnButton() {
    logger.info("Returning to current game");
    for (Entity entity : entityList) {
      entity.setEnabled(true);
    }
    ServiceLocator.getEntityService().getPlayer().getComponent(CombatStatsComponent.class).setImmunity(false);
  }

}