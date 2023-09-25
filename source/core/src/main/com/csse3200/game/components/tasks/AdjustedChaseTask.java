package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Timer;

import static java.lang.Math.abs;

/** Chases a target entity until they get too far away or line of sight is lost. Additional functionality
 * so that it actively checks if entity is stuck behind an object when chasing and adjusts movement. */
public class AdjustedChaseTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final float shootDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;
  private Vector2 lastPos;
  boolean isStuck = false;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public AdjustedChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.shootDistance = 0;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    lastPos = owner.getEntity().getPosition();
    char direction = getDirection(target.getPosition());
    if(direction == '<'){
      this.owner.getEntity().getEvents().trigger("chaseLeft");
    }
    if(direction == '>'||direction == '='){
      this.owner.getEntity().getEvents().trigger("chaseStart");
    }

    Timer.schedule(new Timer.Task(){
      @Override
      public void run() {
        if(getDirection(target.getPosition() )!= direction){
          start();
        }
      }
    },500);

  }

  @Override
  public void update() {
    if (isStuck && movementTask.getStatus() != Status.FINISHED) {

    } else {
      if (lastPos.epsilonEquals(owner.getEntity().getPosition())) {
        isStuck = true;
        movementTask.setTarget(stuckMovement());
      } else {
        isStuck = false;
        movementTask.setTarget(target.getPosition());
      }
      movementTask.update();
      if (movementTask.getStatus() != Status.ACTIVE) {
        movementTask.start();
      }
    }
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
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
    if (dst > maxChaseDistance || dst < shootDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && dst > shootDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  private boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point);
      return false;
    }
    debugRenderer.drawLine(from, to);
    return true;
  }

  /**
   *
   * @param destination
   */
  public char getDirection(Vector2 destination) {
    if (owner.getEntity().getPosition().x - destination.x < 0) {
      return '>';
    }
    if (owner.getEntity().getPosition().x - destination.x > 0) {
      return '<';
    }
    return '=';
  }

  /**
   * Calculates the vector position the entity should move to so that it is not stuck anymore.
   *
   * @return The vector position entity should move to get un stuck.
   */
  public Vector2 stuckMovement() {
    Vector2 currentPos = owner.getEntity().getPosition();
    Vector2 targetPos = owner.getEntity().getPosition();

    Vector2 newPos = owner.getEntity().getPosition();

    if (abs(currentPos.x - targetPos.x) > abs(currentPos.y - targetPos.y)) {
      if (currentPos.y > targetPos.y) {
        newPos.y -= 1;
      } else {
        newPos.y += 1;
      }
    } else {
      if (currentPos.x > targetPos.x) {
        newPos.x -= 1;
      } else {
        newPos.x += 1;
      }
    }

    return newPos;
  }

}
