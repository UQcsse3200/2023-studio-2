package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.BulletFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shoots the bullet when triggered by the AimTask
 */
public class ShootTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);

    private Vector2 spawn;

    boolean hasShot;

    Entity target;

    public ShootTask(Entity target) {
        this.target = target;
        //this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        this.spawn = owner.getEntity().getPosition();
        hasShot = false;
        Entity bullet = BulletFactory.createBullet(target.getPosition());
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
