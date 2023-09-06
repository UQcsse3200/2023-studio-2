package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.EarthGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.ui.MainAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.AlertBox;

import static com.csse3200.game.screens.MainMenuScreen.logger;
import com.csse3200.game.ui.TitleBox;


/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);

  public static GdxGame game;
  private Stage stage; // Add the stage
  private Skin skin;   // Add the skin

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

  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    AlertBox alertBox = new AlertBox(game," Alert Box", skin);
    alertBox.showDialog(stage);
    logger.info("Loading Story");
    TitleBox titleBox = new TitleBox(game,"Story Introduction", skin);
    titleBox.showDialog(stage);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load game");
    //game.setScreen(GdxGame.ScreenType.MAIN_GAME);
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
    mainAlertBox.showDialog(stage, () -> {
      game.setScreen(GdxGame.ScreenType.SPACE_MAP);
    });

  }
  private void onExtractor(){
    logger.info("starting extractor");
    game.setScreen(GdxGame.ScreenType.EXTRACTOR_GAME);
  }

  private void onSpaceMap() {
    logger.info("Launching space map screen");
    game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
  }

}


