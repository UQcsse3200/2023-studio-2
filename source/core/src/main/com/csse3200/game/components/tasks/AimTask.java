package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class AimTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(AimTask.class);
  private final float waitTime;
  private WaitTask waitTask;
  private Task currentTask;
  private ShootTask aimTask;
  private final Entity target;

  /**
   * called.
   *
   * @param waitTime How long in seconds to wait between wandering.
   */
  public AimTask(float waitTime, Entity target) {
    this.waitTime = waitTime;
    this.target = target;
  }

  @Override
  public int getPriority() {
    return 1; // Low priority task
  }

  @Override
  public void start() {
    super.start();

    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    aimTask = new ShootTask(target);
    aimTask.create(owner);

    waitTask.start();
    currentTask = waitTask;

    this.owner.getEntity().getEvents().trigger("standing");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == waitTask) {
        startAiming();
      } else {
        startWaiting();
      }
    }
    currentTask.update();
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(waitTask);
  }


  private void startAiming() {
    logger.debug("Starting aiming");
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(aimTask);
  }

  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }
}

