package com.csse3200.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class RandomUtils {
  public static Vector2 random(Vector2 start, Vector2 end) {
    return new Vector2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
  }

  public static GridPoint2 random(GridPoint2 start, GridPoint2 end) {
    return new GridPoint2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
  }

  private RandomUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
