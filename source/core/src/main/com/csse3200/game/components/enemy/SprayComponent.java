package com.csse3200.game.components.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;


public class SprayComponent extends Component {
    private Entity target;
    private final Entity enemy;
    private float totalTime;

    /**
     * Creates new Spray Attack, will send one instance of projectiles that sprays similar to shotgun bursts
     */
    public SprayComponent(Entity target, Entity enemy) {
        this.target = target;
        this.enemy = enemy;
        float deltaTime = Gdx.graphics.getDeltaTime(); // Get the time since the last frame
        totalTime += deltaTime; // Keep track of total time
    }

    /**
     * Creates multiple projectiles, calculates which position to fire to, sets them off.
     */
    public void sprayAttack() {
        Vector2 ownerPosition = enemy.getPosition();
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
                    Entity bullet = ProjectileFactory.createEnemyBullet(location, enemy);
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
        if (getTimePassed() > 5.0f) {
            totalTime = 0.0f;
            sprayAttack();
        }
    }

    @Override
    public void earlyUpdate() {
        getTimePassed();
    }

    /**
     * Helper Function to return total time since last time reset
     * @return float of total time passed
     */
    public float getTimePassed() {
        float deltaTime = Gdx.graphics.getDeltaTime(); // Get the time since the last frame
        totalTime += deltaTime; // Keep track of total time
        return totalTime;
    }

    /**
     * Helper Function to return if enemy is ready to use spray attack
     * @return Boolean, true if cool down is over
     */
    public boolean isReadyToShoot() {
        return totalTime > 5.0f;
    }

    /**
     * Sets the target to attack
     * @param target the Player's entity
     */
    public void setTarget(Entity target) {
        this.target = target;
    }
}
