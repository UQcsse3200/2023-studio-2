package com.csse3200.game.physics;

import com.csse3200.game.entities.Entity;

/**
 * POJO which contains the custom data attached to each box2D entity. Avoid extending if possible,
 * since the additional references have to be attached to every physics entity.
 */
public class BodyUserData {

  /**
   * The entity to which this body is attached
   */
  public Entity entity;
}
