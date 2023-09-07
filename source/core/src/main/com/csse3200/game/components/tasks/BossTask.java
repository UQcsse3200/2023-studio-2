package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Waits around for a set amount of time and then fires a projectile at a target.
 */
public class BossTask extends DefaultTask implements PriorityTask {

  private final int priority = 5;
  private static final Logger logger = LoggerFactory.getLogger(BossTask.class);
  private Task currentTask;
  private SpecialAttackTask specialAttackTask;
  private WaitTask waitTask;
  private String attackType;


  /**
   * creates an aim task.
   *
   * @param attackType Type of boss attack
   */
  public BossTask(String attackType) {
    this.attackType = attackType;
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
    System.out.println("Working!!!!! yipeee");
    super.start();
    waitTask = new WaitTask(3); //  Can change wait time as long as needed later
    waitTask.create(owner);

    specialAttackTask = new SpecialAttackTask(50f);
    specialAttackTask.create(owner);

    waitTask.start();
    currentTask = waitTask;
    this.owner.getEntity().getEvents().trigger("standing");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == waitTask) {
        startSpecialAttack();
      } else {
        startWaiting();
      }
    }
    currentTask.update();
  }

  /**
   * Returns the priority if task is currently active.
   *
   * @return The current priority.
   */
  private int getActivePriority() {
    return priority;
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
   * Returns the priority if task is currently inactive.
   *
   * @return The current priority.
   */
  private int getInactivePriority() {
    return -1;
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

