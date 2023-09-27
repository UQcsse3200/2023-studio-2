package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.AlertBox;
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
    entity.getEvents().addListener("space map", this::onSpaceMap);
    entity.getEvents().addListener("upgrade shop", this::onShop);
    entity.getEvents().addListener("tutorial", this::onTutorial);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    String startPlanetName = "Verdant Haven";
    logger.info(String.format("Start game, go to %s", startPlanetName));
    PlanetScreen planetScreen = new PlanetScreen(game, startPlanetName);
    ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", planetScreen);
    game.setScreen(planetScreen);

    AlertBox alertBox = new AlertBox(game," Alert Box", skin);
    alertBox.showDialog(stage);

    logger.info("Loading Story");
    TitleBox titleBox = new TitleBox(game,"Story Introduction", skin);
    titleBox.showDialog(stage);
    game.setScreen(GdxGame.ScreenType.INITIALL_SCREEN);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load game");
    //game.setScreen((PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet"));
    game.setScreen(GdxGame.ScreenType.GAME_STORY);

  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }
  private void onTutorial(){
    logger.info("Launching tutorial screen");
    game.setScreen(GdxGame.ScreenType.TUTORIAL_SCREEN);
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

  private void onSpaceMap() {
    logger.info("Launching space map screen");
    ServiceLocator.registerGameStateObserverService(new GameStateObserver());
    ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", new PlanetScreen(game, "Earth"));
    //TODO: Remove once map button is removed from main menu
    game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
  }

  private void onShop() {
    logger.info("Launching Upgrade Shop screen");
    game.setScreen(GdxGame.ScreenType.UPGRADE_SHOP);
  }
}