package com.csse3200.game.ai.movement;

import com.badlogic.gdx.math.Vector2;

/** A movement controller moves something to a given a target. */
public interface MovementController {
  /** @param movementEnabled true to enable controller movement, false to disable. */
  void setMoving(boolean movementEnabled);

  /** @return True when movement enabled, false when disabled */
  boolean getMoving();

  /** @return Target position in the world */
  Vector2 getTarget();

  /** @param target Target position in the world. Controller will steer toward the target. */
  void setTarget(Vector2 target);
}
