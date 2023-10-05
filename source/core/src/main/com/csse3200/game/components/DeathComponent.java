package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PowerupFactory;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ai.tasks.TaskRunner;

import java.util.Random;


/**
 * When this entity (usually killable entities) has health = 0, it disposes the
 * enemy form the field of play.
 */
public class DeathComponent extends Component {
    private CombatStatsComponent combatStats;
    private Boolean notkilled;

    private boolean isDying = false; // True when the entity is currently in the process of dying and playing death
                                     // animation.

    /**
     * Creates a new listener on an entity to wait for kill condition
     */
    @Override
    public void create() {
        entity.getEvents().addListener("updateHealth", this::kill);
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
        this.notkilled = true;
    }

    /**
     * When kill condition met, target entity will be disposed.
     * @param health - the new health of the entity
     */
    public void kill(int health) {
        //check if health is 0, and the kill property has been fulfilled
        if (combatStats.isDead() && this.notkilled) {
            // stop animating the entity once it's death has been confirmed
            this.notkilled = false;
            AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
            animator.stopAnimation();
            Vector2 enemyBody = entity.getCenterPosition();
            entity.getComponent(HitboxComponent.class).setLayer((short) 0);

            entity.getEvents().trigger("dispose");
            // Schedule a task to execute entity::dispose after a delay
            // Get the duration of the death animation
            float deathAnimationDuration = animator.getAnimationDuration("death");
            // Convert the duration from seconds to milliseconds for the Timer
            long delay = (long) (deathAnimationDuration);
            this.isDying = true;

            Timer.Task task = new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(entity::dispose);

                    Random rand = new Random();
                    int powerupRandomiser = rand.nextInt(28);

                    Entity powerup = null;

                    if (powerupRandomiser < 6) { // 3/14 chance of speed boost
                        powerup = PowerupFactory.createPowerup(PowerupType.SPEED_BOOST);
                    } else if (powerupRandomiser < 12) { // 3/14 chance of health boost
                        powerup = PowerupFactory.createPowerup(PowerupType.HEALTH_BOOST);
                    } else if (powerupRandomiser < 14) { // 2/14 chance of temp immunity
                        powerup = PowerupFactory.createPowerup(PowerupType.TEMP_IMMUNITY);
                    } else if (powerupRandomiser < 16) { // 2/14 chance of double damage
                        powerup = PowerupFactory.createPowerup(PowerupType.DOUBLE_DAMAGE);
                    } else if (powerupRandomiser == 17) { // 1/28 chance of extra health
                        powerup = PowerupFactory.createPowerup(PowerupType.EXTRA_LIFE);
                    } else if (powerupRandomiser == 18) { // 1/28 chance of double cross
                        powerup = PowerupFactory.createPowerup(PowerupType.DOUBLE_CROSS);
                    }
                    if (powerup == null) {
                        return;
                    }
                    // 5/14 chance of no powerup dropped
                    if (powerup != null) {
                        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(powerup, enemyBody);
                    }
                }
            };
            Timer.schedule(task, delay);// Delay based on the death animation duration

        }
    }

    /**
     * Gets the current state of the isDying variable.
     *
     * @return true if the entity is in the process of dying and playing the death animation, false otherwise.
     */
    public boolean getIsDying() {
        return isDying;
    }
}