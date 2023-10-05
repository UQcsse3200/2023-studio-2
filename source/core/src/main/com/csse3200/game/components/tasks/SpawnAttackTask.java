package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.GameTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Waits around for a set amount of time and then spawns two entities. Will then spawn new entities once
 * old entities are killed so there are still two entities.
 */
public class SpawnAttackTask extends DefaultTask implements PriorityTask {
  private final int priority;
  private static final Logger logger = LoggerFactory.getLogger(SpawnAttackTask.class);
  private final float waitTime;
  private WaitTask waitTask;
  private Task currentTask;
  private SpawnTask spawnTask;
  private final Entity target;
  private final GameTime timer;
  private long lastSpawnTime;
  private static int numSpawns = 0;
  private static final ArrayList<Entity> entities = new ArrayList<>();

  /**
   * creates a spawn attack task.
   *
   * @param waitTime How long in seconds to wait between spawning enemies.
   * @param target   The target for the enemies
   */
  public SpawnAttackTask(float waitTime, Entity target, int priority) {
    this.waitTime = waitTime;
    this.target = target;
    this.priority = priority;
    this.timer = new GameTime();
    lastSpawnTime = timer.getTime();
  }

  @Override
  public int getPriority() {

    ListIterator<Entity> iterator = entities.listIterator();

    while (iterator.hasNext()) {
      Entity element = iterator.next();
      if (element.getComponent(CombatStatsComponent.class).getHealth() == 0) {
        iterator.remove();
      }
    }
    if (((timer.getTime() - lastSpawnTime > 2f) && entities.size() != 2) || numSpawns == 0) {
      return priority;
    }

    return -1;
  }

  @Override
  public void start() {
    System.out.println("starting");
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
      ListIterator<Entity> iterator = entities.listIterator();

      while (iterator.hasNext()) {
        Entity element = iterator.next();
        if (element.getComponent(CombatStatsComponent.class).getHealth() == 0) {
          iterator.remove();
        }
      }
      if (entities.size() != 2) {
        startSpawning();
      } else {
        startWaiting();
      }
    }
    currentTask.update();
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
   * Calculates the number of enemies to spawn based on the size of the entities array. Then uses the SpawnTask
   * to spawn new enemies appropriately.
   */
  private void startSpawning() {
    System.out.println(entities.size());
    ListIterator<Entity> iterator = entities.listIterator();

    while (iterator.hasNext()) {
      Entity element = iterator.next();
      if (element.getComponent(CombatStatsComponent.class).getHealth() == 0) {
        iterator.remove();
      }
    }

    if (entities.isEmpty()) {
      Entity enemyOne = EnemyFactory.createEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);
      Entity enemyTwo = EnemyFactory.createEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);

      System.out.println("adding enemies");
      entities.add(enemyOne);
      entities.add(enemyTwo);

      logger.debug("Starting spawning");
      this.owner.getEntity().getEvents().trigger("standing");
      lastSpawnTime = timer.getTime();
      numSpawns += 1;

      if (currentTask != null) {
        currentTask.stop();
      }
      currentTask = spawnTask;
      spawnTask.start(entities.get(0), entities.get(1));
    } else if (entities.size() == 1) {
      System.out.println("respawning");
      Entity enemyOne = EnemyFactory.createEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);

      entities.add(enemyOne);

      logger.debug("Starting spawning");
      this.owner.getEntity().getEvents().trigger("standing");
      lastSpawnTime = timer.getTime();
      numSpawns += 1;

      if (currentTask != null) {
        currentTask.stop();
      }
      currentTask = spawnTask;
      spawnTask.start(entities.get(1));
    } else {
      logger.debug("Starting spawning");
      this.owner.getEntity().getEvents().trigger("standing");
      lastSpawnTime = timer.getTime();
      numSpawns += 1;

      if (currentTask != null) {
        currentTask.stop();
      }
      currentTask = spawnTask;
      spawnTask.start();
    }
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


