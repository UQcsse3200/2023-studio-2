package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * Fires Boss' Special Attack
 */
public class SpecialAttackTask extends DefaultTask implements PriorityTask {
    private boolean hasShot;

    private final float radius;

    /**
     * Creates new Special Attack Task
     */
    public SpecialAttackTask(float radius) {
        this.radius = radius;
    }

    @Override
    public void start() {
        super.start();
        hasShot = false;
        // Use special attack
        specialAttack();
        hasShot = true;
    }

    /**
     * Creates multiple projectiles, calculates which position to fire to, sets them off.
     */
    public void specialAttack() {
        Vector2 center = owner.getEntity().getPosition();

        // Define the number of positions you want
        int numPositions = 10;

        // Calculate the angle between each position
        float angleIncrement = 360.0f / numPositions;

        // Create an array to store the positions
        Vector2[] positions = new Vector2[numPositions];
        // Create an array to store the spawn positions
        Vector2[] spawns = new Vector2[numPositions];

        // Calculate the positions
        for (int i = 0; i < numPositions; i++) {
            // Calculate the angle in radians
            float angle = (float) Math.toRadians(i * angleIncrement);

            // Calculate the coordinates of the position
            float x = center.x + this.radius * (float) Math.cos(angle);
            float y = center.y + this.radius * (float) Math.sin(angle);

            // Calculate where to spawn attack
            float spawnX = center.x + 0.25f * (float) Math.cos(angle);
            float spawnY = center.y + 0.25f * (float) Math.sin(angle);

            // Create a Vector2 for the position
            positions[i] = new Vector2(x, y);
            spawns[i] = new Vector2(spawnX, spawnY);
        }
        for (int i = 0; i < numPositions; i++) {
            Entity bullet = ProjectileFactory.createEnemyBullet(positions[i], owner.getEntity());
            ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet, spawns[i]);
        }
    }

    @Override
    public void update() {
        if (hasShot) {
            status = Status.FINISHED;
        }
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
