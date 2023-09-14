package com.csse3200.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for loading resources, e.g. textures, texture atlases, sounds, music, etc. Add new load
 * methods when new types of resources are added to the game.
 */
public class ResourceService implements Disposable {

  private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
  private final AssetManager assetManager;

  public ResourceService() {
    this(new AssetManager());
  }

  /**
   * Initialise this ResourceService to use the provided AssetManager.
   * @param assetManager AssetManager to use in this service.
   * @requires assetManager != null
   */
  public ResourceService(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  /**
   * Load an asset from a file.
   * @param filename Asset path
   * @param type     Class to load into
   * @param <T>      Type of class to load into
   * @return Instance of class loaded from path
   * @see AssetManager#get(String, Class)
   */
  public <T> T getAsset(String filename, Class<T> type) {
    return assetManager.get(filename, type);
  }

  /**
   * Check if an asset has been loaded already
   * @param resourceName path of the asset
   * @param type Class type of the asset
   * @param <T> Type of the asset
   * @return true if asset has been loaded, false otherwise
   * @see AssetManager#contains(String)
   */
  public <T> boolean containsAsset(String resourceName, Class<T> type) {
    return assetManager.contains(resourceName, type);
  }

  /**
   * Returns the loading completion progress as a percentage.
   *
   * @return progress
   */
  public int getProgress() {
    return (int) (assetManager.getProgress() * 100);
  }

  /**
   * Blocking call to load all assets.
   *
   * @see AssetManager#finishLoading()
   */
  public void loadAll() {
    logger.debug("Loading all assets");
    try {
      assetManager.finishLoading();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  /**
   * Loads assets for the specified duration in milliseconds.
   *
   * @param duration duration to load for
   * @return finished loading
   * @see AssetManager#update(int)
   */
  public boolean loadForMillis(int duration) {
    logger.debug("Loading assets for {} ms", duration);
    try {
      return assetManager.update(duration);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return assetManager.isFinished();
  }

  /**
   * Clears all loaded assets and assets in the preloading queue.
   *
   * @see AssetManager#clear()
   */
  public void clearAllAssets() {
    logger.debug("Clearing all assets");
    assetManager.clear();
  }

  /**
   * Loads a single asset into the asset manager.
   *
   * @param assetName asset name
   * @param type      asset type
   * @param <T>       type
   */
  private <T> void loadAsset(String assetName, Class<T> type) {
    logger.debug("Loading {}: {}", type.getSimpleName(), assetName);
    try {
      assetManager.load(assetName, type);
    } catch (Exception e) {
      logger.error("Could not load {}: {}", type.getSimpleName(), assetName);
    }
  }

  /**
   * Loads multiple assets into the asset manager.
   *
   * @param assetNames list of asset names
   * @param type       asset type
   * @param <T>        type
   */
  private <T> void loadAssets(String[] assetNames, Class<T> type) {
    for (String resource : assetNames) {
      loadAsset(resource, type);
    }
  }

  /**
   * Loads a list of texture assets into the asset manager.
   *
   * @param textureNames texture filenames
   */
  public void loadTextures(String[] textureNames) {
    loadAssets(textureNames, Texture.class);
  }

  /**
   * Loads a list of texture atlas assets into the asset manager.
   *
   * @param textureAtlasNames texture atlas filenames
   */
  public void loadTextureAtlases(String[] textureAtlasNames) {
    loadAssets(textureAtlasNames, TextureAtlas.class);
  }

  /**
   * Loads a list of sounds into the asset manager.
   *
   * @param soundNames sound filenames
   */
  public void loadSounds(String[] soundNames) {
    loadAssets(soundNames, Sound.class);
  }

  /**
   * Loads a list of music assets into the asset manager.
   *
   * @param musicNames music filenames
   */
  public void loadMusic(String[] musicNames) {
    loadAssets(musicNames, Music.class);
  }

  public void unloadAssets(String[] assetNames) {
    for (String assetName : assetNames) {
      logger.debug("Unloading {}", assetName);
      try {
        assetManager.unload(assetName);
      } catch (Exception e) {
        logger.error("Could not unload {}", assetName);
      }
    }
  }

  @Override
  public void dispose() {
    assetManager.clear();
  }
}
