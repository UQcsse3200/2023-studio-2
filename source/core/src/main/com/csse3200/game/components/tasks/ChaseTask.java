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

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private float shootDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;
  private List<GridPoint2> path;

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
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
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
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    System.out.println(owner.getEntity().getGridPosition());
    System.out.println(target.getGridPosition());
    path = PathFinder.findPath(owner.getEntity().getGridPosition(), target.getGridPosition());
//    System.out.println(path.get(0));
    for (GridPoint2 grids : path) {
      System.out.println(grids);
    }
    movementTask = new MovementTask(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(0)));
    movementTask.create(owner);
    movementTask.start();
    char direction = getDirection(target.getPosition());
    if(direction == '<'){
      this.owner.getEntity().getEvents().trigger("chaseLeft");
    }
    if(direction == '>'||direction == '='){
      this.owner.getEntity().getEvents().trigger("chaseStart");
    }

    Timer.schedule(new Timer.Task(){
      @Override
      public void run() {
        if(getDirection(target.getPosition() )!= direction){
          start();
        }
      }
    },500);
//    GridPoint2 startPosition = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(owner.getEntity().getPosition());
//    GridPoint2 targetPosition = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(target.getPosition());
//    List<GridPoint2> path = PathFinder.findPath(startPosition, targetPosition);
//    GridPoint2 pathInBetween1 = new GridPoint2(7, 47);
//    GridPoint2 pathInBetween2 = new GridPoint2(8, 48);
//    System.out.println(ServiceLocator.getGameArea().getAreaEntities().get(pathInBetween1));
//    System.out.println(ServiceLocator.getGameArea().getAreaEntities().get(pathInBetween2));
//    for (GridPoint2 grids : path) {
//      System.out.println(grids);
//    }
  }

//  @Override
//  public void update() {
//    path = PathFinder.findPath(owner.getEntity().getGridPosition(), target.getGridPosition());
//    System.out.println(path.get(0));
//    movementTask.setTarget(ServiceLocator.getGameArea().getTerrain().tileToWorldPosition(path.get(0)));
//    movementTask.update();
//    if (movementTask.getStatus() != Status.ACTIVE) {
//      movementTask.start();
//    }
//  }

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
