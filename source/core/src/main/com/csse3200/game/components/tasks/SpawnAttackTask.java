package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Waits around for a set amount of time and then fires a projectile at a target.
 */
public class SpawnAttackTask extends DefaultTask implements PriorityTask {
  private final int priority = 5;
  private static final Logger logger = LoggerFactory.getLogger(SpawnAttackTask.class);
  private final float waitTime;
  private WaitTask waitTask;
  private Task currentTask;
  private SpawnTask spawnTask;
  private final Entity target;
  private final float range;
  private final GameTime timer;
  private long lastSpawnTime;
  private int numSpawns = 0;

  /**
   * creates an aim task.
   *
   * @param waitTime How long in seconds to wait between spawning enemies.
   * @param target   The target for the enemies
   * @param range    The range before entity stops chasing and starts spawning.
   */
  public SpawnAttackTask(float waitTime, Entity target, float range) {
    this.waitTime = waitTime;
    this.target = target;
    this.range = range;
    this.timer = new GameTime();
    lastSpawnTime = timer.getTime();
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

    spawnTask = new SpawnTask(target);
    spawnTask.create(owner);

    waitTask.start();
    currentTask = waitTask;

    this.owner.getEntity().getEvents().trigger("standing");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == waitTask && ((timer.getTime() - lastSpawnTime > 10000) || numSpawns == 0)) {
        startSpawning();
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
    if (dst > range) {
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
    if (dst <= range) {
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
  private void startSpawning() {
    logger.debug("Starting spwning");
    this.owner.getEntity().getEvents().trigger("standing");
    lastSpawnTime = timer.getTime();
    numSpawns = 1;
    swapTask(spawnTask);
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


