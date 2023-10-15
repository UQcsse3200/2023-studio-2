package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.PathFinder;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.DeathComponent;


import java.util.List;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private float shootDistance;
  private PathMovementTask pathMovementTask;
  private List<GridPoint2> path;
  private GridPoint2 targetPosition;
  private char currentDirection;

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
    pathMovementTask = new PathMovementTask(path);
    pathMovementTask.create(owner);
    pathMovementTask.start();
    startDirectionalAnimation();
  }

  @Override
  public void update() {
    Entity entity = owner.getEntity();
    DeathComponent deathComponent = entity.getComponent(DeathComponent.class);
    // Check if the enemy is running death animation before allowing it to chase
    if (deathComponent != null && deathComponent.getIsDying()) {
      // Stop chasing if the enemy is being disposed(delay) and running the death animation
        pathMovementTask.stop();
    } else if (targetPosition.equals(ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getCenterPosition()))){
        char newDirection = getDirection(target.getPosition());
        pathMovementTask.update();
        //check if it reached the destination
        if (pathMovementTask.getStatus().equals(Status.FAILED)) {
            recalculate();
        }
        if (currentDirection != newDirection){
            startDirectionalAnimation();
        }
      } else {
      targetPosition = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getCenterPosition());
      recalculate();
    }
  }

  @Override
  public void stop() {
    super.stop();
    pathMovementTask.stop();
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

  public void startDirectionalAnimation() {
    currentDirection = getDirection(target.getPosition());
    if(currentDirection == '<'){
      this.owner.getEntity().getEvents().trigger("chaseLeft");
    }
    if(currentDirection == '>'|| currentDirection == '='){
      this.owner.getEntity().getEvents().trigger("chaseStart");
    }
  }

  protected void recalculate() {
      //update the path
      path = PathFinder.findPath(owner.getEntity().getGridPosition(), targetPosition);
      //set a new target to the movementtask
      pathMovementTask.setNewPath(path);
  }

}
