package com.csse3200.game.components.maingame;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.LabWindow;
import com.csse3200.game.PauseWindow;
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
  private Array<Entity> freezeList;
  private static final PlayerConfig config =
          FileLoader.readClass(PlayerConfig.class, "configs/player.json");

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
   * Swaps to the Main Menu screen.
   */
  private void onPauseButton() {
    logger.info("Opening Pause Menu");
    PauseWindow pauseWindow = PauseWindow.MakeNewPauseWindow(entity);
    freezeList = ServiceLocator.getEntityService().getEntities();
    System.out.println(freezeList);
    for (Entity pauseTarget : freezeList) {
      if (pauseTarget.getId() != getEntity().getId()) {
        pauseTarget.setEnabled(false);
      }
    }
    ServiceLocator.getRenderService().getStage().addActor(pauseWindow);
}

  private void onExit() {
    logger.info("Exiting main game screen");
    ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "lives", "set", config.lives);
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  protected void onReturnPlanet() {
    logger.info("Exiting to current planet screen");
    ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "lives", "set", config.lives);
    new PlanetTravel(game).returnToCurrent();
  }

  protected void onReturnButton() {
    logger.info("Returning to current game");
    for (Entity pauseTarget : freezeList) {
      pauseTarget.setEnabled(true);
    }
  }

}