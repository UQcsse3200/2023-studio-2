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



    /**
     * Creates a new spawn task.
     */
    public SpawnTask() {}

    /**
     * Starts the spawn task given entities to spawn.
     *
     * @param entities The entities to spawn..
     */
    public void start(ArrayList<Entity> entities) {
        super.start();
        hasSpawned = false;
        spawnEnemy(entities);
        hasSpawned = true;
    }

    /**
     * Spawns entities from a given list with appropriate positioning with respect to owner entity.
     *
     * @param entities The list of entities to be spawned.
     */
    public void spawnEnemy(ArrayList<Entity> entities) {
        if (entities.isEmpty()) {
            // do nothing
        } else if (entities.size() == 1) {
            Vector2 currentPos = owner.getEntity().getPosition();

            Vector2 posOne = new Vector2(currentPos.x + 1, currentPos.y);

            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(entities.get(0), posOne);
        } else if (entities.size() == 2) {
            Vector2 currentPos = owner.getEntity().getPosition();

            Vector2 posOne = new Vector2(currentPos.x + 1, currentPos.y);
            Vector2 posTwo = new Vector2(currentPos.x - 1, currentPos.y);

            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(entities.get(0), posOne);
            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(entities.get(1), posTwo);
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