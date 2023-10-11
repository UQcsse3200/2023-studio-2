package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * Fires Ranged Boss' Projectiles
 */
public class SprayTask extends DefaultTask implements PriorityTask {
    private boolean hasShot;
    private Entity target;

    /**
     * Creates new Spray Task, will send one instance of projectiles that sprays similar to shotgun bursts
     */
    public SprayTask(Entity target) {
        this.target = target;
    }

    @Override
    public void start() {
        super.start();
        hasShot = false;
        // Use special attack
        sprayAttack();
        hasShot = true;
    }

    /**
     * Creates multiple projectiles, calculates which position to fire to, sets them off.
     */
    public void sprayAttack() {
        Vector2 ownerPosition = owner.getEntity().getPosition();
        Vector2[] locations = new Vector2[3];
        locations[0] = this.target.getPosition();
        locations[1] = this.target.getPosition().rotate90(1);
        locations[2] = this.target.getPosition().rotate90(-1);

        final int[] index = {0};
        Timer.Task spawnBulletTask = new Timer.Task() {
            @Override
            public void run() {
                if (index[0] < locations.length) {
                    Vector2 location = locations[index[0]];
                    Entity bullet = ProjectileFactory.createEnemyBullet(location, owner.getEntity());
                    ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet, ownerPosition);
                    index[0]++;
                }
            }
        };

        float delayBetweenFire = 0.1f;
        float initialDelay = 0.0f;
        Timer.schedule(spawnBulletTask, initialDelay, delayBetweenFire);
    }

    @Override
    public void update() {
        if (hasShot) {
            status = Status.FINISHED;
        }
    }

    /**
     * Sets the target to attack
     * @param target the Player's entity
     */
    public void setTarget(Entity target) {
        this.target = target;
    }
    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        // Priority is 8
        return 8;
    }
}
