package com.csse3200.game.services;

import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services.
 * Warning: global access is a trap and should be used <i>extremely</i> sparingly.
 * Read the wiki for details (https://github.com/UQcsse3200/game-engine/wiki/Service-Locator).
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static GameState gameStateService;
  private static TerrainService terrainService;
  private static EntityPlacementService entityPlacementService;
  private static GameStateObserver gameStateObserverService;

  private static StructurePlacementService structurePlacementService;

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }


  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }
  public static GameState getGameStateService(){return gameStateService;}


  public static GameStateObserver getGameStateObserverService() { return gameStateObserverService; }

  public static StructurePlacementService getStructurePlacementService() { return structurePlacementService; }

  public static TerrainService getTerrainService() { return terrainService; }

  public static void registerStructurePlacementService(StructurePlacementService service) {
    logger.debug("Registering stucture placement service {}", service);
    structurePlacementService = service;
  }


  public static EntityPlacementService getEntityPlacementService() { return entityPlacementService; }

  public static void registerEntityPlacementService(EntityPlacementService service) {
    logger.debug("Registering entity placement service {}", service);
    entityPlacementService = service;
  }

  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerTerrainService(TerrainService source) {
    logger.debug("Registering game state service {}", source);
    terrainService = source;
  }


  public static void registerGameStateObserverService(GameStateObserver source) {
    logger.debug("Registering game state observer service {}", source);
    gameStateObserverService = source;
  }

  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    gameStateService = null;
    gameStateObserverService = null;
    terrainService = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
