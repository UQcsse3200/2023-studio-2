package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Shoots a projectile at a target in game.
 */
public class SpecialAttackTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);

    private Vector2 spawn;

    private boolean hasShot;
    private final int priority = 8;

    private final float radius;

    /**
     * Creates a new shoot task.
     */
    public SpecialAttackTask(float radius) {
        this.radius = radius;
    }

    @Override
    public void start() {
        super.start();
        owner.getEntity().getEvents().trigger("standing");
        this.spawn = owner.getEntity().getPosition();
        hasShot = false;
        // Use special attack
        specialAttack();
        hasShot = true;
    }

    /**
     * Fires projectile in game.
     */
    public void specialAttack() {
        Vector2 center = owner.getEntity().getPosition();

        // Define the radius of the circle

        // Define the number of positions you want
        int numPositions = 10;

        // Calculate the angle between each position
        float angleIncrement = 360.0f / numPositions;

        // Create an array to store the positions
        Vector2[] positions = new Vector2[numPositions];

        // Calculate the positions
        for (int i = 0; i < numPositions; i++) {
            // Calculate the angle in radians
            float angle = (float) Math.toRadians(i * angleIncrement);

            // Calculate the coordinates of the position
            float x = center.x + this.radius * (float) Math.cos(angle);
            float y = center.y + this.radius * (float) Math.sin(angle);

            // Create a Vector2 for the position
            positions[i] = new Vector2(x, y);
        }

        for (int i = 0; i < numPositions; i++) {
            Entity bullet = ProjectileFactory.createEnemyBullet(positions[i], owner.getEntity());
            ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(bullet, spawn);
        }
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

    @Override
    public int getPriority() {
        return priority;
    }
}
