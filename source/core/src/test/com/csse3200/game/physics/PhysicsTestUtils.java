package com.csse3200.game.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.physics.components.ColliderComponent;
import net.dermetfan.utils.Pair;

public class PhysicsTestUtils {
  public static Vector2 getBounds(PolygonShape shape) {
    Pair<Vector2, Vector2> corners = getCorners(shape);
    Vector2 min = corners.getKey();
    Vector2 max = corners.getValue();

    return max.sub(min);
  }

  public static Vector2 getRectanglePosition(ColliderComponent collider) {
    Shape shape = collider.getFixture().getShape();
    assertTrue(shape instanceof PolygonShape);
    return getRectanglePosition((PolygonShape)shape);
  }

  /**
   * Returns the bottom left corner of the shape's bounding box
   * @param shape Shape with fixture
   * @return Bottom left corner position
   */
  public static Vector2 getRectanglePosition(PolygonShape shape) {
    Pair<Vector2, Vector2> corners = getCorners(shape);
    return corners.getKey();
  }

  /**
   * Given an entity, test that it's collider contains a PolygonShape of the given size
   * @param collider Collider with a fixture
   * @param scale Required bounding box of the PolygonShape
   */
  public static void checkPolygonCollider(ColliderComponent collider, Vector2 scale) {
    Shape shape = collider.getFixture().getShape();
    assertTrue(shape instanceof PolygonShape);

    Vector2 bounds = PhysicsTestUtils.getBounds((PolygonShape) shape);
    assertEquals(scale, bounds);
  }

  private static Pair<Vector2, Vector2> getCorners(PolygonShape shape) {
    Vector2 min = Vector2Utils.MAX.cpy();
    Vector2 max = Vector2Utils.MIN.cpy();
    Vector2 vect = new Vector2();

    for (int i = 0; i < shape.getVertexCount(); i++) {
      shape.getVertex(i, vect);
      min.x = Math.min(min.x, vect.x);
      min.y = Math.min(min.y, vect.y);

      max.x = Math.max(max.x, vect.x);
      max.y = Math.max(max.y, vect.y);
    }

    return new Pair<>(min, max);
  }
}
