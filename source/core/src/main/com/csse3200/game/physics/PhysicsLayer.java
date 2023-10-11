package com.csse3200.game.physics;

public class PhysicsLayer {
  public static final short NONE = 0;
  @SuppressWarnings("PointlessBitwiseExpression")
  public static final short DEFAULT = (1 << 0);
  public static final short PLAYER = (1 << 1);

  public static final short COMPANION = (1 << 1);
  public static final short DISPLAY_ONLY = (1 << 11);
  public static final short ITEMS_ABOVE_PLATFORM = (1 << 12);
  public static final short SHIP = (1<<1);
  public static final short SLIDER = (1<<1);
  public static final short LABORATORY = (1<<5);

  // Terrain obstacle, e.g. trees
  public static final short BOX = ~0;
  public static final short OBSTACLE = (1 << 2);
  // NPC (Non-Playable Character) colliders
  public static final short NPC_OBSTACLE = (1 << 2);
  public static final short NPC = (1 << 3);
  // NPC enemy colliders
  public static final short ENEMY_RANGE = (1 << 6);
  public static final short ENEMY_MELEE = (1 << 8);

  public static final short ENEMY_PROJECTILE = (1 << 9);
  public static final short STRUCTURE = (1 << 5);
  public static final short WALL = (1 << 4);
  public static final short TURRET = (1<<4);

  public static final short WEAPON = (1 << 7);

  public static final short ALL = ~0;

  public static boolean contains(short filterBits, short layer) {
    return (filterBits & layer) != 0;
  }

  private PhysicsLayer() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
