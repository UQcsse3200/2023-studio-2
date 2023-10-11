package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Spawns enemies into map which will chase target.
 */
public class SpawnTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(SpawnTask.class);
    private boolean hasSpawned;
    Entity target;


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
        super.start();
        hasSpawned = false;
        spawnEnemy();
        hasSpawned = true;
    }

    /**
     * Starts the spawn task given entities to spawn.
     *
     * @param enemyOne The entity to spawn.
     */
    public void start(Entity enemyOne) {
        super.start();
        hasSpawned = false;
        spawnEnemy(enemyOne);
        hasSpawned = true;
    }

    /**
     * Starts the spawn task given entities to spawn.
     *
     * @param enemyOne The first entity to spawn.
     * @param enemyTwo The second entity to spawn.
     */
    public void start(Entity enemyOne, Entity enemyTwo) {
        super.start();
        hasSpawned = false;
        spawnEnemy(enemyOne, enemyTwo);
        hasSpawned = true;
    }

    /**
     * Does not do anything as no entities need to be spawned.
     */
    public void spawnEnemy() {
        // Do nothing
    }

    /**
     * Spawns one entity 1 unit to the right of the owner entity.
     *
     * @param enemyOne The entity to be spawned.
     */
    public void spawnEnemy(Entity enemyOne) {
        Vector2 currentPos = owner.getEntity().getPosition();

        Vector2 posOne = new Vector2(currentPos.x + 1, currentPos.y);

        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemyOne, posOne);
    }

    /**
     * Spawns two entities. 1 unit to the right and left of the owner entity.
     *
     * @param enemyOne The first entity to be spawned.
     * @param enemyTwo The second entity to be spawned.
     */
    public void spawnEnemy(Entity enemyOne, Entity enemyTwo) {
        Vector2 currentPos = owner.getEntity().getPosition();

        Vector2 posOne = new Vector2(currentPos.x + 1, currentPos.y);
        Vector2 posTwo = new Vector2(currentPos.x - 1, currentPos.y);

        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemyOne, posOne);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemyTwo, posTwo);
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