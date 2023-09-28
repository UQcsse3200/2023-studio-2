package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * Fires Boss' Special Attack
 */
public class SprayTask extends DefaultTask implements PriorityTask {
    private boolean hasShot;

    private float radius;
    private Vector2 target;

    /**
     * Creates new Special Attack Task
     */
    public SprayTask(Vector2 target, float radius) {
        this.target = target;
        this.radius = radius;
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
        // Calculate the direction vector towards the player
        Vector2 targetPlayerBullet = this.target, offsetBulletOne, offsetBulletTwo;

        // Calculate the directions for the other two bullets at ±45 degrees
        float angleOffset = 60; // Angle offset in degrees

//        offsetBulletOne.rotateDeg(angleOffset);
//        offsetBulletTwo.rotateDeg(-angleOffset);

        // Create and spawn the bullets
        Entity bullet1 = ProjectileFactory.createEnemyBullet(targetPlayerBullet, owner.getEntity());
//        Entity bullet2 = ProjectileFactory.createEnemyBullet(offsetBulletOne, owner.getEntity());
//        Entity bullet3 = ProjectileFactory.createEnemyBullet(offsetBulletTwo, owner.getEntity());

        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet1, ownerPosition);
//        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet2, ownerPosition);
//        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet3, ownerPosition);
    }

    @Override
    public void update() {
        if (hasShot) {
            status = Status.FINISHED;
        }
    }

    public void setTarget(Vector2 target) {
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
