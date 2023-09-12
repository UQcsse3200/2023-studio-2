package com.csse3200.game.physics.raycast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;

/**
 * Cast a ray against all colliders that match the layer mask. All hits will be stored, with an empty
 * array if no hits occurred.
 */
public class AllHitCallback implements RayCastCallback {
  private final Array<RaycastHit> raycastHits;
  public short layerMask = ~0;

  public AllHitCallback() {
    this.raycastHits = new Array<>(false, 4);
  }

  public RaycastHit[] getHitsAndClear() {
    RaycastHit[] hits = raycastHits.toArray();
    raycastHits.clear();
    return hits;
  }

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    if ((fixture.getFilterData().categoryBits & layerMask) != 0) {
      RaycastHit hit = new RaycastHit();
      hit.fixture = fixture;
      hit.normal = normal;
      hit.point = point;
      raycastHits.add(hit);
      return fraction;
    }
    return 1;
  }
}
