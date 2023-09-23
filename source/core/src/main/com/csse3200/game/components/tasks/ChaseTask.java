package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.PathFinder;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;
import java.util.List;

import static com.csse3200.game.components.npc.PathFinder.findPath;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private float shootDistance;
  private MovementTask movementTask;
  private List<GridPoint2> path;
  private GridPoint2 targetPosition;
  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.shootDistance = 0;
  }

  /**
   * Creates a new chase task which will stop once the entity is within a certain distance of the target.
   *
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   * @param shootDistance The distance where the entity stops to shoot at the target.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float shootDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.shootDistance = shootDistance;
  }

  @Override
  public void start() {
    super.start();
    targetPosition = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getCenterPosition());
    //get the list of grids which is the path to take to reach the target
    path = PathFinder.findPath(owner.getEntity().getGridPosition(), targetPosition);
    //create the movementTask and input the vector position of the first tile of the path
    movementTask = new MovementTask(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(0)));
    movementTask.create(owner);
    movementTask.start();
  }

  @Override
  public void update() {
    movementTask.update();
    //check if it reached the tile (the first tile of the path in the start() function)
    if (movementTask.getStatus() != Status.ACTIVE) {
      //if the tile was reach then find the new targetlocation
      targetPosition = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getCenterPosition());
      //check if we're already at the tile or not
      if (!owner.getEntity().getGridPosition().equals(targetPosition)) {
        //if we are not at the player tile then find the new path
        path = PathFinder.findPath(owner.getEntity().getGridPosition(), targetPosition);
        //set a new target to the movementtask
        movementTask.setTarget(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(0)));
        //start the movementtask again
        movementTask.start();
      } else {
        movementTask.update();
      }
    } else {
      //if we didn't reach the tile yet
      //then check whether we should continue to go to the tile or just find a new path, check by seeing if the targets location has changed
      if (!(targetPosition.equals(ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getCenterPosition())))) {
        if (owner.getEntity().getGridPosition().equals(targetPosition)) {
          return;
        } else {
          //if it has changed then stop the movementtask
          movementTask.stop();
          //find the new targetlocation
          targetPosition = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getCenterPosition());
          if (!owner.getEntity().getGridPosition().equals(targetPosition)) {
            path = PathFinder.findPath(owner.getEntity().getGridPosition(), targetPosition);
            movementTask.setTarget(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(0)));
            movementTask.start();
          }
        }
      } else {
        movementTask.update();
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
    if (dst > maxChaseDistance || dst < shootDistance) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && dst > shootDistance) {
      return priority;
    }
    return -1;
  }

  /**
   * This get method returns a char indicating the position of the target relative to the enemy.
   * @param destination
   * @return
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
}
