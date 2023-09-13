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

import java.util.Timer;
import java.util.TimerTask;

/** Chases a target entity until they get too far away or line of sight is lost */
public class RunTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float runDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   */
  public RunTask(Entity target, int priority, float runDistance) {
    this.target = target;
    this.priority = priority;
    this.runDistance = runDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(getVectorTo());
    movementTask.create(owner);
    movementTask.start();
    char direction = getDirection(target.getPosition());
    if(direction == '<'){
      this.owner.getEntity().getEvents().trigger("chaseLeft");
    }
    if(direction == '>'||direction == '='){
      this.owner.getEntity().getEvents().trigger("chaseStart");
    }
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
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
    movementTask.setTarget(getVectorTo());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
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
    if (dst > runDistance) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < runDistance) {
      return priority;
    }
    return -1;
  }

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
   * Calculates the desired vector position to move away from the target
   *
   * @return The vector position.
   */
  private Vector2 getVectorTo(){
    float currentX = owner.getEntity().getPosition().x;
    float currentY = owner.getEntity().getPosition().y;

    float targetX = target.getPosition().x;
    float targetY = target.getPosition().y;

    float newX = currentX + (currentX - targetX);
    float newY = currentY + (currentY - targetY);

    Vector2 newPos = new Vector2(newX, newY);
    return newPos;
  }
}

