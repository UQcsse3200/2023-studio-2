package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.abs;

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
        fireProjectile(target.getPosition());
        hasShot = true;
    }

    /**
     * Fires projectile in game.
     *
     * @param position The position where the projectile is fired at.
     */
    public void fireProjectile(Vector2 position) {
        Vector2 currentPos = owner.getEntity().getPosition();

        if (currentPos.y < position.y) {
            spawn.y += 1;
        }
        if (currentPos.x < position.x) {
            spawn.x += 1;
        } else {
            spawn.x -= 0.5;
        }

        // Calculate the square where bullet should be fired to
        Vector2 targetLocation = target.getPosition();
        float finalX;
        float finalY;
        if (targetLocation.x == spawn.x) {
            finalX = targetLocation.x;
            finalY = targetLocation.y + 1 * (targetLocation.y - spawn.y) / abs(targetLocation.y - spawn.y);
        } else {
            float gradient = (targetLocation.y - spawn.y) / (targetLocation.x - spawn.x);
            finalX = targetLocation.x + 1 * (targetLocation.x - spawn.x) / abs(targetLocation.x - spawn.x);
            finalY = targetLocation.y + gradient * 1 * (targetLocation.x - spawn.x) / abs(targetLocation.x - spawn.x);
        }

        Entity bullet = ProjectileFactory.createEnemyBullet(new Vector2(finalX, finalY), owner.getEntity());

        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet, spawn);
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
