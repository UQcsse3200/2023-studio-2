package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.enemy.SprayTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Timer;

/**
 * BossTask controls actions of the Boss Entity. Responsible for attacking
 * and movement.
 */
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
  private char direction;
  private EnemyType bossType;
  private SprayTask sprayTask;
  private WaitTask waitTask;
  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public BossTask(EnemyType bossType, Entity target, int priority, float viewDistance, float maxChaseDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.bossType = bossType;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  /**
   * Used to check if Boss has used special attack outside of class
   * @return True if Boss has already used special attack
   */
  public boolean isUnleashed() {
      return unleashed;
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    float attackRadius = 10;
    // Boss Type
    if (this.bossType == EnemyType.BossMelee) {
      specialAttackTask = new SpecialAttackTask(attackRadius);
      specialAttackTask.create(owner);
    } else if (this.bossType == EnemyType.BossRanged) {
      // Shoots spray periodically
      waitTask = new WaitTask(3);
      waitTask.create(owner);
      sprayTask = new SprayTask(target.getPosition(), 5);
      sprayTask.create(owner);
    }

    // Standard Movement
    currentTask = movementTask;
    movementTask.start();
    direction = getDirection(target.getPosition());
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
  }

  @Override
  public void update() {
    float currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
    float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
    if (currentHealth <= 0) {
      this.owner.getEntity().getEvents().trigger("standing");
    }
    // Boss Melee
    if (this.bossType == EnemyType.BossMelee) {
      // Check if health is below threshold
      if ((currentHealth / maxHealth) * 100 <= 50.0f && !unleashed) {
        System.out.println("Special Attack Incoming");
        attackReady = true;
      }
      if (attackReady && !unleashed) {
        unleashed = true;
        System.out.println("Unleash!");
        startSpecialAttack();
      }
    }
    // Boss Ranged
    if (this.bossType == EnemyType.BossRanged) {

      if (waitTask.getStatus() == Status.ACTIVE) {
        waitTask.update();
      }
      else if (waitTask.getStatus() != Status.ACTIVE) {
        sprayTask.setTarget(target.getPosition());
        sprayTask.update();
        startSprayAttack();
        waitTask.start();
      }
    }

    char direction2 = getDirection(target.getPosition());
    movementTask.setTarget(target.getPosition());
    movementTask.update();

    if (movementTask.getStatus() != Status.ACTIVE) {
      startMovement();
    }
    if (direction != direction2){
      start();
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

  /**
   * Will return a float distance of how far the Boss is from their target
   * @return float number as distance
   */
  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * Will get the current task's priority value
   * @return priority value as integer, if Boss not in range or target not visible, return -1
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  /**
   * Will get the currently inactive priority value
   * @return priority value as integer, if Boss not in range or target not visible, return -1
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  /**
   * Checks if a target is in line of sight to the Boss.
   * @return true or false, if target is in sight of Boss
   */
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

  /**
   * Converts the Vector2 positions to one of a few characters that represent certain directions.
   * @param destination final destination for the Boss.
   * @return a character that represents which direction Boss should face.
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

  /**
   * Swaps current task with a new task. Used for controlling Boss' current actions.
   * @param newTask The task to swap to.
   */
  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  /**
   * Helper method to initiate Special Attack
   */
  private void startSpecialAttack() {
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(specialAttackTask);
  }
  /**
   * Helper method to initiate Spray Attack
   */
  private void startSprayAttack() {
    this.owner.getEntity().getEvents().trigger("standing");
    swapTask(sprayTask);
  }

  /**
   * Helper method to initiate Movement
   */
  private void startMovement() {
    movementTask.start();
    swapTask(movementTask);
  }
}







