package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 * <p>
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 16;

  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);

  private Array<Entity> getEntities() {
    return entities;
  }

  /**
   * Gets the player entity in the EntityService
   *
   * @return Entity - the player entity, or null - no player found in EntityService
   */
  public Entity getPlayer() {
    for (Entity entity : entities) {
      if (entity.getEntityType().equals("player")) {
        return entity;
      }
    }
    return null;
  }

  /**
   * Gets all entities belonging to a specified component class.
   * <p>
   * Example:
   *    getEntitiesByComponent(PowerupComponent.class) will return a list
   *    of all registered powerup entities.
   *
   * @param componentClass The component class to search for
   * @return An array of entities who have the specified component
   */
  public List<Entity> getEntitiesByComponent(Class<? extends Component> componentClass) {
    List<Entity> filteredEntities = new ArrayList<>();

    for (Entity entity : new  Array.ArrayIterator<>(entities)) {
      if (entity.getComponent(componentClass) != null) {
        filteredEntities.add(entity);
      }
    }
    return filteredEntities;
  }

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
    entity.create();
  }

  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    entities.removeValue(entity, true);
  }

  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  public void update() {
    for (Entity entity : entities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  /**
   * Dispose all entities.
   */
  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }
  public Entity getCompanion() {
    for (Entity entity : entities) {
      if (entity.getEntityType().equals("companion")) {
        return entity;
      }
    }
    return null;
  }

}
