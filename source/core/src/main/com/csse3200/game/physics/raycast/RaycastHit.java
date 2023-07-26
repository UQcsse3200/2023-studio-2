package com.csse3200.game.physics.raycast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

/** Stores information about a raycast hit. */
public class RaycastHit {

  /** Fixture which was hit. */
  public Fixture fixture;

  /** Point at which the raycast hit the fixture. */
  public Vector2 point;

  /** the normal vector of the collider surface at the hit point. */
  public Vector2 normal;
}
