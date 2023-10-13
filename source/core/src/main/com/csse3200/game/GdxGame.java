package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.csse3200.game.screens.TutorialScreen;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.screens.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  private ScreenType screenType;

  @Override
  public void create() {
    logger.info("Creating game");
    Gdx.graphics.setTitle("Escape Earth");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

    setScreen(ScreenType.MAIN_MENU);
    this.screenType = ScreenType.MAIN_MENU;
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
    this.screenType = screenType;
  }

  /**
   * Returns current screen that game is displaying.
   * @return ScreenType
   */
  public ScreenType getScreenType() {
    return this.screenType;
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case GAME_STORY:
        return new StoryScreen(this);
      case SETTINGS:
        return new SettingsScreen(this);
      case SPACE_MAP:
        return new SpaceMapScreen(this);
      case TUTORIAL_SCREEN:
        return new TutorialScreen(this,"Tutorial");
        case EXTRACTOR_GAME:
        return new ExtractorMiniGameScreen(this);
      case PLAYER_DEATH_0:
        return new PlayerDeathScreen(this, 0);
      case PLAYER_DEATH_1:
        return new PlayerDeathScreen(this, 1);
      case PLAYER_DEATH_2:
        return new PlayerDeathScreen(this, 2);
      case PLAYER_DEATH_3:
        return new PlayerDeathScreen(this, 3);
      case COMPANION_DEATH:
        return new CompanionDeathScreen(this);
      case MINI_SCREEN:
        return new MiniScreen(this);
      case SPACEMINI_SCREEN:
        return new MiniScreen(this);
      case NAVIGATION_SCREEN:
        return new SpaceNavigationScreen(this);
      case UPGRADE_SHOP:
        return new UpgradeShopScreen(this);
      case CONTROL_SCREEN:
        return new ControlsScreen(this,"Controls");
      case CONTROL_SCREEN_GAME:
        return new ControlsScreen(this,"Controls");
      case INITIAL_SCREEN:
        return new InitialScreen(this);
      case BRICK_BREAKER_SCREEN:
        return new BrickBreakerScreen(this);
      default:
        return null;
    }
  }

  public enum ScreenType {

    MAIN_MENU, SETTINGS, TITLE_SCREEN, SPACE_MAP, CONTROL_SCREEN, EXTRACTOR_GAME, TUTORIAL_SCREEN,
    GAME_STORY, PLAYER_DEATH,COMPANION_DEATH, NAVIGATION_SCREEN ,MINI_SCREEN, SPACEMINI_SCREEN, UPGRADE_SHOP, INITIAL_SCREEN, BRICK_BREAKER_SCREEN,
    PLAYER_DEATH_0, PLAYER_DEATH_1, PLAYER_DEATH_2, PLAYER_DEATH_3, CONTROL_SCREEN_GAME
  }
  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
    System.exit(0);
  }
}
