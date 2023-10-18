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
    private Vector2 posOne;
    private Vector2 posTwo;
    private Entity target;
    private boolean hasSpawned;

    /**
     * Creates a new spawn task.
     */
    public SpawnTask(Entity target) {
        this.target = target;
    }

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
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = target.getPosition();

        if (Math.abs(targetPos.x - currentPos.x) > Math.abs(targetPos.y - currentPos.y)) {
            posOne = new Vector2(currentPos.x, currentPos.y + 1);
            posTwo = new Vector2(currentPos.x, currentPos.y - 1);
        } else {
            posOne = new Vector2(currentPos.x + 1, currentPos.y);
            posTwo = new Vector2(currentPos.x - 1, currentPos.y);
        }

        if (posOne.x < 0 || posOne.x > 90 || posOne.y < 0 || posOne.y > 90) {
            posOne = posTwo.cpy();
        }

        if (posTwo.x < 0 || posTwo.x > 90 || posTwo.y < 0 || posTwo.y > 90) {
            posTwo = posOne.cpy();
        }

        char attackDirection = getDirection(target.getPosition());
        if(attackDirection == '<'){
            owner.getEntity().getEvents().trigger("attackLeft");
        }
        if(attackDirection == '>'||attackDirection == '='){
            owner.getEntity().getEvents().trigger("enemyAttack");
        }

        if (entities.size() == 1) {
            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(entities.get(0), posOne);
        } else if (entities.size() == 2) {
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