package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Waits around for a set amount of time and then fires a projectile at a target.
 */
public class BossTask extends DefaultTask implements PriorityTask {

  private final int priority = 7;
  private static final Logger logger = LoggerFactory.getLogger(AimTask.class);
  private final float waitTime;
  private WaitTask waitTask;
  private Task currentTask;
  private SpecialAttackTask specialAttackTask;
  private final float radius;
  private Entity target;
  private boolean unleashed;
  private boolean attackReady;


  /**
   * creates an aim task.
   *
   * @param waitTime How long in seconds to wait between firing a projectile.
   * @param radius Radius of the special attack
   */
  public BossTask(float waitTime, float radius, Entity target) {
    this.target = target;
    this.waitTime = waitTime;
    this.radius = radius;
  }

  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  @Override
  public void start() {
    super.start();

    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);

    specialAttackTask = new SpecialAttackTask(this.radius);
    specialAttackTask.create(owner);

    waitTask.start();
    currentTask = waitTask;

    this.owner.getEntity().getEvents().trigger("standing");
  }

  @Override
  public void update() {
    if (unleashed) {
      status = Status.FINISHED;
    }
    // Check if health is below threshold
    float currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
    float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
    if ((currentHealth / maxHealth) * 100 <= 50.0f && !unleashed) {
      System.out.println("Special Attack Incoming");
      attackReady = true;
    }

    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == waitTask) {
//        if (attackReady && !unleashed) {
//          attackReady = false;
//          unleashed = true;
//          startSpecialAttack();
//        }
        if (attackReady) {
          startSpecialAttack();
        }
      } else {
        startWaiting();
      }
    }
    currentTask.update();
  }

  /**
   * Returns the distance between the current entity and the target location.
   *
   * @return The distance between the owner's entity and the target location.
   */
  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * Returns the priority if task is currently active.
   *
   * @return The current priority.
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > 3) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  /**
   * Returns the priority if task is currently inactive.
   *
   * @return The current priority.
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst <= 3) {
      return priority;
    }
    return -1;
  }

  /**
   * Makes the entity wait.
   */
  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(waitTask);
  }

  /**
   * Makes the entity aim.
   */
  private void startSpecialAttack() {
    logger.debug("Starting attack");
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(specialAttackTask);
  }

  /**
   * Stops the old task being performed and starts the new one.
   *
   * @param newTask The task to be performed.
   */
  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }
}

