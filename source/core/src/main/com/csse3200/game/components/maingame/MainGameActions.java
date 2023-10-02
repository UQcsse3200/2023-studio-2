package com.csse3200.game.components.maingame;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
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
  private static final PlayerConfig config =
          FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  public MainGameActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("returnPlanet", this::onReturnPlanet);
  }
  /**
   * Swaps to the Main Menu screen.
   */
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

}