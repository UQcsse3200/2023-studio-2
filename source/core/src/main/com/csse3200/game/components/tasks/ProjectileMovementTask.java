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
public class ProjectileMovementTask extends ChaseTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final DebugRenderer debugRenderer;
  private MovementTask movementTask;

  /**
   * @param target The entity to target.
   * @param priority Task priority when moving (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which the movement can start.
   * @param maxChaseDistance Maximum distance from the entity while moving before giving up.
   */
  public ProjectileMovementTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance);
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
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
    return owner.getEntity().getPosition().dst(target.getPosition());
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
    Vector2 to = target.getCenterPosition();

    debugRenderer.drawLine(from, to);
    return true;
  }
}
