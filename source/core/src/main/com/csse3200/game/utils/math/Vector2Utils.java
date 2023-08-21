package com.csse3200.game.utils.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Contains additional utility constants and functions for common Vector2 operations.
 */
public class Vector2Utils {
  public static final Vector2 LEFT = new Vector2(-1f, 0f);
  public static final Vector2 RIGHT = new Vector2(1f, 0f);
  public static final Vector2 UP = new Vector2(0f, 1f);
  public static final Vector2 DOWN = new Vector2(0f, -1f);

  public static final Vector2 ONE = new Vector2(1f, 1f);
  public static final Vector2 MAX = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
  public static final Vector2 MIN = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);

  /**
   * Calculate the angle in degrees of a vector.
   *
   * @param vector The vector relative to the origin
   * @return Angle in degrees from -180 to 180
   */
  public static double angleTo(Vector2 vector) {
    return Math.toDegrees(Math.atan2(vector.y, vector.x));
  }

  /**
   * Calculate the angle in degrees between two vectors
   *
   * @param from The vector from which angle is measured
   * @param to The vector to which angle is measured
   * @return Angle in degrees from -180 to 180
   */
  public static double angleFromTo(Vector2 from, Vector2 to) {
    return Math.toDegrees(Math.atan2(to.y - from.y, to.x - from.x));
  }

  private Vector2Utils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
