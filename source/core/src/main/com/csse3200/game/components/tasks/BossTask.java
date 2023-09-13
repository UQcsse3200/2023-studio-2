//package com.csse3200.game.components.tasks;
//
//import com.csse3200.game.ai.tasks.AITaskComponent;
//import com.csse3200.game.ai.tasks.DefaultTask;
//import com.csse3200.game.ai.tasks.PriorityTask;
//import com.csse3200.game.ai.tasks.Task;
//import com.csse3200.game.components.CombatStatsComponent;
//import com.csse3200.game.entities.Entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Waits around for a set amount of time and then fires a projectile at a target.
// */
//public class BossTask extends DefaultTask implements PriorityTask {
//
//  private final int priority = 7;
//  private static final Logger logger = LoggerFactory.getLogger(AimTask.class);
//  private final float waitTime;
//  private WaitTask waitTask;
//  private Task currentTask;
//  private SpecialAttackTask specialAttackTask;
//  private final float radius;
//  private Entity target;
//  private boolean unleashed;
//  private boolean attackReady;
//
//
//  /**
//   * creates an aim task.
//   *
//   * @param waitTime How long in seconds to wait between firing a projectile.
//   * @param radius Radius of the special attack
//   */
//  public BossTask(float waitTime, float radius, Entity target) {
//    this.target = target;
//    this.waitTime = waitTime;
//    this.radius = radius;
//  }
//
//  @Override
//  public int getPriority() {
//    if (status == Status.ACTIVE) {
//      return getActivePriority();
//    }
//
//    return getInactivePriority();
//  }
//
//  @Override
//  public void start() {
//    super.start();
//
//    waitTask = new WaitTask(waitTime);
//    waitTask.create(owner);
//
//    specialAttackTask = new SpecialAttackTask(this.radius);
//    specialAttackTask.create(owner);
//
//    waitTask.start();
//    currentTask = waitTask;
//
//    this.owner.getEntity().getEvents().trigger("standing");
//  }
//
//  @Override
//  public void update() {
//    if (unleashed) {
//      status = Status.FINISHED;
//    }
//    // Check if health is below threshold
//    float currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
//    float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
//    if ((currentHealth / maxHealth) * 100 <= 50.0f && !unleashed) {
//      System.out.println("Special Attack Incoming");
//      attackReady = true;
//    }
//
//    if (currentTask.getStatus() != Status.ACTIVE) {
//      if (currentTask == waitTask) {
////        if (attackReady && !unleashed) {
////          attackReady = false;
////          unleashed = true;
////          startSpecialAttack();
////        }
//        if (attackReady) {
//          startSpecialAttack();
//        }
//      } else {
//        startWaiting();
//      }
//    }
//    currentTask.update();
//  }
//
//  /**
//   * Returns the distance between the current entity and the target location.
//   *
//   * @return The distance between the owner's entity and the target location.
//   */
//  private float getDistanceToTarget() {
//    return owner.getEntity().getPosition().dst(target.getPosition());
//  }
//
//  /**
//   * Returns the priority if task is currently active.
//   *
//   * @return The current priority.
//   */
//  private int getActivePriority() {
//    float dst = getDistanceToTarget();
//    if (dst > 3) {
//      return -1; // Too far, stop chasing
//    }
//    return priority;
//  }
//
//  /**
//   * Returns the priority if task is currently inactive.
//   *
//   * @return The current priority.
//   */
//  private int getInactivePriority() {
//    float dst = getDistanceToTarget();
//    if (dst <= 3) {
//      return priority;
//    }
//    return -1;
//  }
//
//  /**
//   * Makes the entity wait.
//   */
//  private void startWaiting() {
//    logger.debug("Starting waiting");
//    this.owner.getEntity().getEvents().trigger("standing");
//    swapTask(waitTask);
//  }
//
//  /**
//   * Makes the entity aim.
//   */
//  private void startSpecialAttack() {
//    logger.debug("Starting attack");
//    this.owner.getEntity().getEvents().trigger("standing");
//    swapTask(specialAttackTask);
//  }
//
//  /**
//   * Stops the old task being performed and starts the new one.
//   *
//   * @param newTask The task to be performed.
//   */
//  private void swapTask(Task newTask) {
//    if (currentTask != null) {
//      currentTask.stop();
//    }
//    currentTask = newTask;
//    currentTask.start();
//  }
//}
//
package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.CombatStatsComponent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/** Chases a target entity until they get too far away or line of sight is lost */
public class BossTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;
  private Task currentTask;
  private SpecialAttackTask specialAttackTask;
  private boolean unleashed;
  private boolean attackReady;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public BossTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    float attackRadius = 10;
    specialAttackTask = new SpecialAttackTask(attackRadius);
    specialAttackTask.create(owner);

    currentTask = movementTask;
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
    // Check if health is below threshold
    float currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
    float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
    if (currentHealth <= 0) {
      this.owner.getEntity().getEvents().trigger("standing");
    }
    if ((currentHealth / maxHealth) * 100 <= 50.0f && !unleashed) {
      System.out.println("Special Attack Incoming");
      attackReady = true;
    }
    if (attackReady && !unleashed) {
      unleashed = true;
      System.out.println("Unleash!");
      startSpecialAttack();
    } else if (currentTask.getStatus() != Status.ACTIVE) {
      // start moving
      System.out.println("Started Moving");
      startMovement();
    }
    currentTask.update();
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

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point);
      return false;
    }
    debugRenderer.drawLine(from, to);
    return true;
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
  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }
  private void startSpecialAttack() {
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(specialAttackTask);
  }

  private void startMovement() {
    movementTask.setTarget(target.getPosition());
    swapTask(movementTask);
  }
}







