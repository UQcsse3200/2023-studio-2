package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

public class RingBurstComponent extends Component {

    private boolean hasShot;
    private final Entity enemy;

    /**
     * Creates new Ring Burst Attack, which only activates when the enemy
     * is below 50% hp.
     */
    public RingBurstComponent(Entity enemy) {
        this.enemy = enemy;
    }

    /**
     * Creates multiple projectiles, calculates which position to fire to, sets them off.
     */
    public void ringBurstAttack() {
        Vector2 center = enemy.getPosition();

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
            float radius = 10f;
            float x = center.x + radius * (float) Math.cos(angle);
            float y = center.y + radius * (float) Math.sin(angle);

            // Create a Vector2 for the position
            positions[i] = new Vector2(x, y);
        }
        Timer.Task spawnBulletTask = getBulletTask(positions);

        float delayBetweenFire = 0.1f;
        float initialDelay = 0.0f;
        Timer.schedule(spawnBulletTask, initialDelay, delayBetweenFire);
    }

    /**
     * Helper Function to create a task that will spawn bullets periodically
     * @param positions All the vector positions to shoot bullets
     * @return Task to send bullets, which can be activated by starting task
     */
    private Timer.Task getBulletTask(Vector2[] positions) {
        final int[] index = {0};
        return new Timer.Task() {
            @Override
            public void run() {
                if (index[0] < positions.length) {
                    Vector2 location = positions[positions.length - index[0] - 1];
                    Entity bullet = ProjectileFactory.createEnemyBullet(location, enemy);
                    ServiceLocator.getStructurePlacementService().spawnEntityAtVector(bullet, enemy.getPosition());
                    index[0]++;
                }
            }
        };
    }

    @Override
    public void update() {
        // Check if enemy is at 50%
        float currentHealth = enemy.getComponent(CombatStatsComponent.class).getHealth();
        float maxHealth = enemy.getComponent(CombatStatsComponent.class).getMaxHealth();
        // Check if health is below threshold
        if ((currentHealth / maxHealth) * 100 <= 50.0f && !hasShot) {
            ringBurstAttack();
            hasShot = true;
        }
    }

}
