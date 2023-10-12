package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.ConfigLoader;
import com.csse3200.game.areas.mapConfig.GameConfig;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.ui.Popups.ChoicePopup;
import com.csse3200.game.ui.Popups.PopupBox;
import com.csse3200.game.utils.LoadUtils;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.MainAlert;
import com.csse3200.game.ui.TitleBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  public static GdxGame game;
  private final Stage stage; // Add the stage
  private final Skin skin;   // Add the skin

  public MainMenuActions(GdxGame game, Stage stage, Skin skin) { // Modify the constructor
    this.game = game;
    this.stage = stage; // Initialize stage
    this.skin = skin;   // Initialize skin
  }


  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("space minigame", this::onMini);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("extractor minigame",this::onExtractor);
    entity.getEvents().addListener("upgrade shop", this::onShop);
    entity.getEvents().addListener("tutorial", this::onTutorial);
    entity.getEvents().addListener("brick breaker minigame", this::onBrickBreaker);

  }

  private void loadGameConfig() {
      logger.info("Loading in GameConfig");

      GameConfig gameConfig = null;
      try {
          gameConfig = ConfigLoader.loadGame();
          if (gameConfig.levelNames.isEmpty()) throw new InvalidConfigException(LoadUtils.NO_LEVELS_ERROR);
      } catch (Exception e) {
          logger.error("Failed to load game - not leaving to game screen.");
          return;
      }

      String firstPlanet;
      String secondPlanet = null;

      if (gameConfig.gameState != null && !gameConfig.gameState.isEmpty()) {
          ServiceLocator.getGameStateObserverService().loadGameStateMap(gameConfig.gameState);
          firstPlanet = (String) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet");
          secondPlanet = (String) ServiceLocator.getGameStateObserverService().getStateData("nextPlanet");
      } else {
          firstPlanet = gameConfig.levelNames.get(0);
          if (gameConfig.levelNames.size() > 1) {
              PlanetScreen tempFirstPlanet = new PlanetScreen(game, gameConfig.levelNames.get(0));
              secondPlanet = tempFirstPlanet.getNextPlanetName();
          }
      }
      ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", firstPlanet);
      ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "nextPlanet", secondPlanet);
  }

  private void loadGame() {
      loadGameConfig();
      new PlanetTravel(game).returnToCurrent();
  }

  private void newGame(){

      Gdx.files.local("save").deleteDirectory();

      loadGameConfig();

    logger.info("Loading Story");
    game.setScreen(GdxGame.ScreenType.INITIALL_SCREEN);
  }

  /**
   * Swaps to the Tutorial screen.
   */

  private void onTutorial(){
    logger.info("Loading Tutorial");
    TitleBox titleBox = new TitleBox(game, "Tutorial","Hey! This is the tutorial of the game.",skin);
    titleBox.showDialog(stage);
    game.setScreen(GdxGame.ScreenType.TUTORIAL_SCREEN);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart(boolean previousSave) {
      if (previousSave) {
          ChoicePopup popup = new ChoicePopup("A save file already exists, starting a new game will overwrite this existing file", "Existing Save", skin);
          popup.show(stage);
          popup.getEvents().addListener(popup.getChoice1(), this::newGame);
      } else {
      newGame();
      }
  }


  /**
   * Loading a saved game state.
   */
  private void onLoad(boolean validLoad) {
    if (validLoad) {
        logger.info("Loading: Fetching from previous save");
        loadGame();
    } else {
        logger.info("Loading: Failed to find save file");
        PopupBox popupBox = new PopupBox("No previous load found", "Load", skin);
        popupBox.show(stage);
    }
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.SETTINGS);
  }
  private void onMini(){
    logger.info("starting space minigame");
    MainAlert mainAlertBox = new MainAlert(game, "Start game", skin, "Ready to play the game");
    mainAlertBox.showDialog(stage, () -> game.setScreen(GdxGame.ScreenType.SPACEMINI_SCREEN));

  }
  private void onExtractor(){
    logger.info("starting extractor");
    game.setScreen(GdxGame.ScreenType.EXTRACTOR_GAME);
  }

  private void onShop() {
    logger.info("Launching Upgrade Shop screen");
    game.setScreen(GdxGame.ScreenType.UPGRADE_SHOP);
  }
  private void onBrickBreaker(){
    logger.info("Starting brick breaker minigame screen");
    game.setScreen(GdxGame.ScreenType.BRICK_BREAKER_SCREEN);
  }
}