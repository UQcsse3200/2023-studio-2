package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.EntityPlacementService;
import com.csse3200.game.services.StructurePlacementService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area
 * has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>
 * Support for enabling/disabling game areas could be added by making this a
 * Component instead.
 */
public abstract class GameArea implements Disposable {
  protected static TerrainComponent terrain;
  protected HashMap<GridPoint2, Entity> pathFinderGrids;
  protected List<Entity> areaEntities;
  protected Entity companion;
  protected static Entity player;
  protected EntityPlacementService entityPlacementService;
  protected StructurePlacementService structurePlacementService;
  protected ArrayList<Entity> targetables;
  protected EventHandler handler;

  protected GameArea() {
    handler = new EventHandler();
    areaEntities = new ArrayList<>();
    pathFinderGrids = new HashMap<>();
    this.targetables = new ArrayList<>();
  }

  /**
   * Create the game area in the world.
   */
  public abstract void create();

  /**
   * Dispose of all internal entities in the area
   */
  public void dispose() {
    for (var entity : areaEntities) {
      if (entity.getDisposed()) {
        continue;
      }
      entity.dispose();
    }
  }

  public TerrainComponent getTerrain() {
    return terrain;
  }

  public Map<GridPoint2, Entity> getAreaEntities() {
    return pathFinderGrids;
  }

  public EventHandler getEvent(){
    return handler;
  }

  protected void registerStructurePlacementService() {
    EventHandler handler = new EventHandler();
    structurePlacementService = new StructurePlacementService(handler);
    ServiceLocator.registerStructurePlacementService(structurePlacementService);
    handler.addListener("spawnExtractor", this::spawnExtractor);
    handler.addListener("placeStructure", this::spawnEntity);
    handler.addListener("fireBullet",
        (StructurePlacementService.spawnEntityAtVectorArgs args) -> spawnEntityAtVector(args.getEntity(),
            args.getWorldPos()));
    handler.addListener("placeStructureAt",
        (StructurePlacementService.placeStructureAtArgs args) -> spawnEntityAt(args.getEntity(), args.getTilePos(),
            false, false));
  }

  /**
   * Spawn a given extractor at its current position
   * Includes handling the animation of the extractor
   *
   * @param entity Entity (not yet registered) representing the extractor
   */
  protected void spawnExtractor(Entity entity) {
    this.spawnEntity(entity);
  }

  /**
   * Function to register entity placement service using teh approripate
   * listeners.
   * This allows entities to be placed after initilisation.
   */
  protected void registerEntityPlacementService() {
    EventHandler handler = new EventHandler();
    entityPlacementService = new EntityPlacementService(handler);
    ServiceLocator.registerEntityPlacementService(entityPlacementService);
    handler.addListener("placeEntity", this::spawnEntity);
    handler.addListener("placeEntityAt", this::placeEntityAt);
  }

  /**
   * Function to listen for "placeEntityAt" trigger and repond by
   * placing entitiy at specified position.
   *
   * @param entity   - Entity to be placed
   * @param position - position for where entity should be placed
   */
  protected void placeEntityAt(Entity entity, Vector2 position) {
    entity.setPosition(position);
    spawnEntity(entity);
  }

  /**
   * Spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  protected void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    if ((terrain != null && entity.getComponent(ColliderComponent.class) != null)
        && (PhysicsLayer.contains(entity.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.LABORATORY) ||
            PhysicsLayer.contains(entity.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.OBSTACLE) ||
            PhysicsLayer.contains(entity.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.SHIP))) {
      Vector2 position = entity.getPosition();
      Vector2 scale = entity.getScale();

      // Calculate the four corners of the entity
      Vector2 topRight = new Vector2(position.x + scale.x, position.y + scale.y);
      Vector2 bottomLeft = new Vector2(position.x, position.y);

      // Convert these to grid coordinates
      GridPoint2 gridTopRight = terrain.worldPositionToTile(topRight);
      GridPoint2 gridBottomLeft = terrain.worldPositionToTile(bottomLeft);

      // Iterate through the grid coordinates and add them to the HashMap
      for (int x = gridBottomLeft.x; x < gridTopRight.x; x++) {
        for (int y = gridBottomLeft.y; y < gridTopRight.y; y++) {
          GridPoint2 gridPoint = new GridPoint2(x, y);
          pathFinderGrids.putIfAbsent(gridPoint, entity);
        }
      }
    }
    ServiceLocator.getEntityService().register(entity);

    if (entity.getComponent(HitboxComponent.class) != null) {
      if (PhysicsLayer.contains(entity.getComponent(HitboxComponent.class).getLayer(), PhysicsLayer.STRUCTURE)) {
        handler.trigger("reTarget");
      }
    }
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity  Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom
   *                left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom
   *                left corner
   */
  protected void spawnEntityAt(
      Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    float tileSize = terrain.getTileSize();

    if (centerX) {
      worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
    }
    if (centerY) {
      worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
    }

    entity.setPosition(worldPos);
    spawnEntity(entity);
  }

  protected void spawnEntityAtVector(Entity entity, Vector2 worldPos) {
    entity.setPosition(worldPos);
    spawnEntity(entity);
  }

  public Entity getCompanion() {
    return companion;
  }

  public static Entity getPlayer() {
    return player;
  }
}