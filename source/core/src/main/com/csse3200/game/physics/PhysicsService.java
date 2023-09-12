package com.csse3200.game.physics;

/**
 * Provides a global access point to the physics engine. This is necessary for physics-based
 * entities to add or remove themselves from the world, as well as update their position each frame.
 */
public class PhysicsService {
  private final PhysicsEngine engine;

  public PhysicsService() {
    this(new PhysicsEngine());
  }

  public PhysicsService(PhysicsEngine engine) {
    this.engine = engine;
  }

  public PhysicsEngine getPhysics() {
    return engine;
  }
}
