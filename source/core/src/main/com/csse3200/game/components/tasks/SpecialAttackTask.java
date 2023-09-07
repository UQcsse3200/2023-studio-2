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
public class SpecialAttackTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);

    private Vector2 spawn;

    private boolean hasShot;

    Entity boss;

    /**
     * Creates a new shoot task.
     *
     * @param boss The target Entity which the projectile will be fired at.
     */
    public SpecialAttackTask(Entity boss) {
        this.boss = boss;
    }

    @Override
    public void start() {
        super.start();
        this.spawn = owner.getEntity().getPosition();
        hasShot = false;
        //fireProjectile(target.getPosition()); // this is where we want to know where to shoot the projectile
        hasShot = true;
    }

    /**
     * Fires projectile in game.
     *
     * @param position The position where the projectile is fired at.
     */
    public void fireProjectile(Vector2 position) {
        Vector2 currentPos = owner.getEntity().getPosition();
        // please change boss variable to something else later
        Entity bullet = ProjectileFactory.createEnemyBullet(boss.getPosition(), owner.getEntity());
        if (currentPos.y < position.y) {
            spawn.y += 1;
        }
        ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(bullet, spawn);
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
