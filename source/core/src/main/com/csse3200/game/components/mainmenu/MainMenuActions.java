package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.ConfigLoader;
import com.csse3200.game.areas.mapConfig.GameConfig;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.files.FileLoader;
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
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("space minigame", this::onMini);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("extractor minigame",this::onExtractor);
    entity.getEvents().addListener("upgrade shop", this::onShop);
  }

  private void loadGameConfig(boolean newGame) {
      logger.info("Loading in GameConfig");

      GameConfig gameConfig;
      try {
          gameConfig = ConfigLoader.loadGame();
          if (gameConfig.levelNames.isEmpty()) throw new InvalidConfigException(LoadUtils.NO_LEVELS_ERROR);
          ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", gameConfig.levelNames.get(0));
          if (gameConfig.levelNames.size() > 1) {
              PlanetScreen firstPlanet = new PlanetScreen(game, gameConfig.levelNames.get(0));
              ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "nextPlanet", firstPlanet.getNextPlanetName());
          }
      } catch (Exception e) {
          logger.error("Failed to load game - not leaving to game screen.");
      }
  }

  private void loadGame() {
      loadGameConfig(false);
      new PlanetTravel(game).returnToCurrent();
  }

  private void newGame(){

      Gdx.files.local("save").deleteDirectory();

      loadGameConfig(true);

      logger.info("Loading Story");
      TitleBox titleBox = new TitleBox(game,"Story Introduction", skin);
      titleBox.showDialog(stage);
      game.setScreen(GdxGame.ScreenType.INITIALL_SCREEN);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart(boolean previousSave) {
      if (previousSave) {
          ChoicePopup popup = new ChoicePopup("A save file already exists, starting a new game will overwrite this existing file", "Existing Save", skin);
          popup.show(stage);
          popup.getEvents().addListener(popup.getChoice1(), this::newGame);
          return;
      }
      newGame();
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
        PopupBox popupBox =  new PopupBox("No previous load found", "Load", skin);
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
}