package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Spawns enemies into map which will chase target.
 */
public class SpawnTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(SpawnTask.class);
    private boolean hasSpawned;
    Entity target;
    private ArrayList<Entity> entities;


    /**
     * Creates a new spawn task.
     *
     * @param target The target Entity which the spawned entities will target.
     */
    public SpawnTask(Entity target) {
        this.target = target;
    }

    @Override
    public void start() {
        entities = new ArrayList<>();
        super.start();
        hasSpawned = false;
        spawnEnemy();
        hasSpawned = true;
    }

    /**
     * Spawn enemies.
     */
    public void spawnEnemy() {
        Vector2 currentPos = owner.getEntity().getPosition();

        ListIterator<Entity> iterator = entities.listIterator();

        while (iterator.hasNext()) {
            Entity element = iterator.next();
            if (element.getComponent(CombatStatsComponent.class).getHealth() == 0) {
                iterator.remove();
            }
        }
        if (entities.size() == 0) {
            Entity enemyOne = EnemyFactory.createEnemy(EnemyType.Melee, EnemyBehaviour.PTE);
            Entity enemyTwo = EnemyFactory.createEnemy(EnemyType.Melee, EnemyBehaviour.PTE);

            entities.add(enemyOne);
            entities.add(enemyTwo);

            Vector2 posOne = new Vector2(currentPos.x + 1, currentPos.y);
            Vector2 posTwo = new Vector2(currentPos.x - 1, currentPos.y);

            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemyOne, posOne);
            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemyTwo, posTwo);
        } else if (entities.size() == 1) {
            Entity enemyOne = EnemyFactory.createEnemy(EnemyType.Melee, EnemyBehaviour.PTE);

            entities.add(enemyOne);

            Vector2 posOne = new Vector2(currentPos.x + 1, currentPos.y);

            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemyOne, posOne);
        }
    }

    @Override
    public void update() {
        if (hasSpawned) {
            status = Status.FINISHED;
            logger.debug("Finished spawning");
        }
    }

    @Override
    public void stop() {
        super.stop();
        logger.debug("Stopping spawn");
    }
}
