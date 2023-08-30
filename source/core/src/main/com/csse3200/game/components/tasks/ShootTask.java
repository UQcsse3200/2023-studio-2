package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shoots a projectile at a target in game.
 */
public class ShootTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);

    private Vector2 spawn;

    private boolean hasShot;

    Entity target;

    /**
     * Creates a new shoot task.
     *
     * @param target The target Entity which the projectile will be fired at.
     */
    public ShootTask(Entity target) {
        this.target = target;
    }

    @Override
    public void start() {
        super.start();
        this.spawn = owner.getEntity().getPosition();
        hasShot = false;
        Entity bullet = ProjectileFactory.createEnemyBullet(target.getPosition(), owner.getEntity());
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
