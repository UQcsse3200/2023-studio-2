package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.Timer;
import java.util.TimerTask;

/** Runs away from target entity if it gets too close */
public class RunTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float runDistance;
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
    PhysicsEngine physics = ServiceLocator.getPhysicsService().getPhysics();
    DebugRenderer debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    owner.getEntity().getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(2f, 2f));
    movementTask = new MovementTask(getVectorTo());
    movementTask.create(owner);
    movementTask.start();
    char direction = getDirection(getVectorTo());
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
    owner.getEntity().getComponent(PhysicsMovementComponent.class).changeMaxSpeed(Vector2Utils.ONE);
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

  private char getDirection(Vector2 destination) {
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

