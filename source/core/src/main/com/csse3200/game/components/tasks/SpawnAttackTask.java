package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyName;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.GameTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ListIterator;

import static com.csse3200.game.entities.enemies.EnemyName.redGhost;

/**
 * Waits around for a set amount of time and then spawns two entities. Will then spawn new entities once
 * old entities are killed so there are still two entities.
 */
public class SpawnAttackTask extends DefaultTask implements PriorityTask {
  private final int priority;
  private static final Logger logger = LoggerFactory.getLogger(SpawnAttackTask.class);
  private final float waitTime;
  private final int maxSpawnCount;
  private Entity target;
  private WaitTask waitTask;
  private Task currentTask;
  private SpawnTask spawnTask;
  private final ArrayList<Entity> entities = new ArrayList<>();

  /**
   * creates a spawn attack task.
   *
   * @param waitTime How long in seconds to wait between spawning enemies.
   * @param priority the priority scale for the task
   * @param maxSpawnCount the maximum amount of enemies to spawn
   */
  public SpawnAttackTask(Entity target, float waitTime, int priority, int maxSpawnCount) {
    this.target = target;
    this.waitTime = waitTime;
    this.priority = priority;
    this.maxSpawnCount = maxSpawnCount;
  }

  @Override
  public int getPriority() {
    updateEnemyList();
    if (entities.size() != maxSpawnCount) {
      return priority;
    }
    return -1;
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
      updateEnemyList();
      if (currentTask == spawnTask) {
        startWaiting();
      } else {
        if (entities.size() != maxSpawnCount) {
          startSpawning();
        } else {
          startWaiting();
        }
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
    updateEnemyList();

    ArrayList<Entity> entitiesToSpawn = new ArrayList<>();

    if (maxSpawnCount - entities.size() >= 2) {
      Entity enemyOne = EnemyFactory.createEnemy(redGhost);
      Entity enemyTwo = EnemyFactory.createEnemy(redGhost);

      entities.add(enemyOne);
      entities.add(enemyTwo);

      entitiesToSpawn.add(enemyOne);
      entitiesToSpawn.add(enemyTwo);

    } else if (maxSpawnCount - entities.size() == 1) {
      Entity enemyOne = EnemyFactory.createEnemy(redGhost);

      entities.add(enemyOne);

      entitiesToSpawn.add(enemyOne);
    }
    logger.debug("Starting spawning");
    this.owner.getEntity().getEvents().trigger("standing");

    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = spawnTask;
    spawnTask.start(entitiesToSpawn);
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

  private void updateEnemyList() {
    ListIterator<Entity> iterator = entities.listIterator();

    while (iterator.hasNext()) {
      Entity element = iterator.next();
      if (element.getComponent(CombatStatsComponent.class).getHealth() == 0) {
        iterator.remove();
      }
    }
  }
}


