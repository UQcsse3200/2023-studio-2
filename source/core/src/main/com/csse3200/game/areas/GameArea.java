package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.EntityPlacementService;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;
  protected EntityPlacementService entityPlacementService;
  protected StructurePlacementService structurePlacementService;

  protected GameArea() {
    areaEntities = new ArrayList<>();
  }

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }
  }

  protected void registerStructurePlacementService() {
    EventHandler handler = new EventHandler();
    structurePlacementService = new StructurePlacementService(handler);
    ServiceLocator.registerStructurePlacementService(structurePlacementService);
    handler.addListener("spawnExtractor", this::spawnExtractor);
    handler.addListener("placeStructure", this::spawnEntity);
    handler.addListener("fireBullet",
            (StructurePlacementService.SpawnEntityAtVectorArgs args) ->
                    spawnEntityAtVector(args.entity, args.worldPos)
  );
    handler.addListener("placeStructureAt",
            (StructurePlacementService.PlaceStructureAtArgs args) ->
                    spawnEntityAt(args.entity, args.tilePos, args.centerX, args.centerY)
    );
  }

  /**
   * Spawn a given extractor at its current position
   * Includes handling the animation of the extractor
   *
   * @param entity Entity (not yet registered) representing the extractor
   */
  protected void spawnExtractor(Entity entity) {
    // TODO: Right now this just passes to spawnEntity but in future will have additional functionality
    this.spawnEntity(entity);
  }

  /**
   * Function to register entity placement service using teh approripate listeners.
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
   * @param entity - Entity to be placed
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
    ServiceLocator.getEntityService().register(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom left corner
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
}
