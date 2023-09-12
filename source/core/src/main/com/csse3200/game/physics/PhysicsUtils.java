package com.csse3200.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

public class PhysicsUtils {

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
        .getComponent(ColliderComponent.class)
        .setAsBoxAligned(
            boundingBox, PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.BOTTOM);
  }

  /**
   * Modifies the collider of an entity to be a rectangle of a specified size with the corner
   * being in the specified relative coordinates
   * @param entity the entity the collider is bound to
   * @param sizeX the length of the new collision box
   * @param sizeY the height of the new collision box
   * @param posX the relative x coordinate of the top left corner of the collision box
   * @param posY the relative y coordinate of the top left corner of the collision box
   */
  public static void setCustomCollider(Entity entity, float sizeX, float sizeY, float posX, float posY) {
    Vector2 currentBox = entity.getScale().cpy();
    Vector2 pos = new Vector2(posX, posY);
    Vector2 boundingBox = entity.getScale().cpy().scl(sizeX, sizeY);
    entity.getComponent(ColliderComponent.class).setAsBox(boundingBox, pos);
  }

  private PhysicsUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
