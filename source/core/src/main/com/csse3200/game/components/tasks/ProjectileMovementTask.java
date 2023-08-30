package com.csse3200.game.components.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

/** Bullet moves towards the target in a straight line until it reaches the target position  */
public class ProjectileMovementTask extends DefaultTask implements PriorityTask {
  private Vector2 targetLocation;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final DebugRenderer debugRenderer;
  private MovementTask movementTask;

  /**
   * @param targetLocation The location where the projectile will go to.
   * @param priority Task priority when moving (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which the movement can start.
   * @param maxChaseDistance Maximum distance from the entity while moving before giving up.
   */
  public ProjectileMovementTask(Vector2 targetLocation, int priority, float viewDistance, float maxChaseDistance) {
    this.targetLocation = targetLocation;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(targetLocation);
    movementTask.create(owner);
    movementTask.start();

    this.owner.getEntity().getEvents().trigger("chaseStart");
  }

  public void update() {
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      Gdx.app.postRunnable(owner.getEntity()::dispose);
    }
  }

  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(targetLocation);
  }

  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  private boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = targetLocation;

    debugRenderer.drawLine(from, to);
    return true;
  }
}
