package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class WanderAndShootTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WanderAndShootTask.class);

  private final Vector2 wanderRange;
  private final float waitTime;
  private Vector2 startPos;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;

  private AimTask aimTask;

  private final Entity target;

  /**
   * @param wanderRange Distance in X and Y the entity can move from its position when start() is
   *     called.
   * @param waitTime How long in seconds to wait between wandering.
   */
  public WanderAndShootTask(Vector2 wanderRange, float waitTime, Entity target) {
    this.wanderRange = wanderRange;
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
    startPos = owner.getEntity().getPosition();

    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange());
    movementTask.create(owner);
    aimTask = new AimTask(target);
    aimTask.create(owner);

    movementTask.start();
    currentTask = movementTask;

    this.owner.getEntity().getEvents().trigger("wanderStart");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask) {
        startWaiting();
      } else if (currentTask == waitTask){
        startAiming();
      } else {
        startMoving();
      }
    }
    currentTask.update();
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(waitTask);
  }

  private void startMoving() {
    logger.debug("Starting moving");
    Vector2 newPosition = getRandomPosInRange();
    char direction = getDirection(newPosition);

    if(direction == '<'){
      this.owner.getEntity().getEvents().trigger("wander_left");
    }
    if(direction == '>'||direction == '='){
      this.owner.getEntity().getEvents().trigger("wanderStart");
    }
    if(direction == '='){
      this.owner.getEntity().getEvents().trigger("standing");
    }
    movementTask.setTarget(newPosition);
    swapTask(movementTask);
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

  private Vector2 getRandomPosInRange() {
    Vector2 halfRange = wanderRange.cpy().scl(0.5f);
    Vector2 min = startPos.cpy().sub(halfRange);
    Vector2 max = startPos.cpy().add(halfRange);
    return RandomUtils.random(min, max);
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
}
