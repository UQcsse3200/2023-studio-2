package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

  private final GameTime gameTime;
  private Vector2 target;
  private float stopDistance = 0.01f;
  private long lastTimeMoved;
  private Vector2 lastPos;
  private PhysicsMovementComponent movementComponent;

  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
  }

  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  @Override
  public void start() {
    super.start();
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
    movementComponent.setTarget(target);
    movementComponent.setMoving(true);
    logger.debug("Starting movement towards {}", target);
    lastTimeMoved = gameTime.getTime();
    lastPos = owner.getEntity().getPosition();
  }

  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false);
      status = Status.FINISHED;
      logger.debug("Finished moving to {}", target);
    } else {
      checkIfStuck();
    }
  }

  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }

  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
    logger.debug("Stopping movement");
  }

  private boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

  private void checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime();
      lastPos = owner.getEntity().getPosition();
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false);
      status = Status.FAILED;
      logger.debug("Got stuck! Failing movement task");
    }
  }

  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
  }
}
