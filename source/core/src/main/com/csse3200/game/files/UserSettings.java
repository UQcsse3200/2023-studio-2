package com.csse3200.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.csse3200.game.files.FileLoader.Location;

import java.io.File;

/**
 * Reading, Writing, and applying user settings in the game.
 */
public class UserSettings {
  private static final String ROOT_DIR = "CSSE3200Game";
  private static final String SETTINGS_FILE = "settings.json";

  private static final int WINDOW_WIDTH = 1280;
  private static final int WINDOW_HEIGHT = 800;

  /**
   * Get the stored user settings
   * @return Copy of the current settings
   */
  public static Settings get() {
    String path = ROOT_DIR + File.separator + SETTINGS_FILE;
    Settings fileSettings = FileLoader.readClass(Settings.class, path, Location.EXTERNAL);
    // Use default values if file doesn't exist
    return fileSettings != null ? fileSettings : new Settings();
  }

  /**
   * Set the stored user settings
   * @param settings New settings to store
   * @param applyImmediate true to immediately apply new settings.
   */
  public static void set(Settings settings, boolean applyImmediate) {
    String path = ROOT_DIR + File.separator + SETTINGS_FILE;
    FileLoader.writeClass(settings, path, Location.EXTERNAL);

    if (applyImmediate) {
      applySettings(settings);
    }
  }

  /**
   * Apply the given settings without storing them.
   * @param settings Settings to apply
   */
  public static void applySettings(Settings settings) {
    Gdx.graphics.setForegroundFPS(settings.fps);
    Gdx.graphics.setVSync(settings.vsync);

    if (settings.fullscreen) {
      DisplayMode displayMode = findMatching(settings.displayMode);
      if (displayMode == null) {
        displayMode = Gdx.graphics.getDisplayMode();
      }
      Gdx.graphics.setFullscreenMode(displayMode);
    } else {
      Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
  }

  private static DisplayMode findMatching(DisplaySettings desiredSettings) {
    if (desiredSettings == null) {
      return null;
    }
    for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
      if (displayMode.refreshRate == desiredSettings.refreshRate
          && displayMode.height == desiredSettings.height
          && displayMode.width == desiredSettings.width) {
        return displayMode;
      }
    }

    return null;
  }

  /**
   * Stores game settings, can be serialised/deserialised.
   */
  public static class Settings {
    /**
     * FPS cap of the game. Independant of screen FPS.
     */
    public int fps = 60;
    public boolean fullscreen = true;
    public boolean vsync = true;
    /**
     * ui Scale. Currently unused, but can be implemented.
     */
    public float uiScale = 1f;
    public DisplaySettings displayMode = null;
  }

  /**
   * Stores chosen display settings. Can be serialised/deserialised.
   */
  public static class DisplaySettings {
    public int width;
    public int height;
    public int refreshRate;

    public DisplaySettings() {}

    public DisplaySettings(DisplayMode displayMode) {
      this.width = displayMode.width;
      this.height = displayMode.height;
      this.refreshRate = displayMode.refreshRate;
    }
  }

  private UserSettings() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
