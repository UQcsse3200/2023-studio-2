package com.csse3200.game.physics.raycast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Cast a ray against all colliders that match the layermask. The closest hit will be stored, or
 * none if no hit occurred.
 */
public class SingleHitCallback implements RayCastCallback {
  public short layerMask = ~0;
  public RaycastHit hit;
  public boolean didHit;

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    if ((fixture.getFilterData().categoryBits & layerMask) != 0) {
      didHit = true;
      hit.fixture = fixture;
      hit.point = point;
      hit.normal = normal;
      return fraction; // Continue in case of closer object
    }
    return 1; // Ignore this collision, it wasn't in the layer mask.
  }
}
