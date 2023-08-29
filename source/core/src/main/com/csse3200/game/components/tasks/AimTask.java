package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.BulletFactory;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class AimTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(AimTask.class);

    private Vector2 spawn;

    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;

    boolean hasShot;

    Entity target;

    public AimTask(Entity target) {
        this.target = target;
        //this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        this.spawn = owner.getEntity().getPosition();
        hasShot = false;
        Entity bullet = BulletFactory.createBullet(target);
        ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(bullet, spawn);
        hasShot = true;
    }

    @Override
    public void update() {
        if (hasShot) {
            status = Status.FINISHED;
            logger.debug("Finished aiming");
        }
    }

    @Override
    public void stop() {
        super.stop();
        logger.debug("Stopping aim");
    }

}
