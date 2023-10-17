package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PowerupFactory;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Random;

/**
 * When this entity (usually killable entities) has health = 0, it disposes the
 * enemy form the field of play.
 */
public class DeathComponent extends Component {
    private CombatStatsComponent combatStats;
    private Boolean notkilled;
    private Random rand = new Random();
    private boolean isDying = false; // True when the entity is currently in the process of dying and playing death
    // animation.

    /**
     * Creates a new listener on an entity to wait for the kill condition.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("updateHealth", this::kill);
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
        this.notkilled = true;
    }

    /**
     * When the kill condition is met, the target entity will be disposed.
     *
     * @param health - the new health of the entity
     */
    public void kill(int health) {
        // Check if health is 0, and the kill property has been fulfilled
        if (combatStats.isDead() && this.notkilled) {
            // Stop animating the entity once its death has been confirmed
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
                    int powerUpRandomizer = rand.nextInt(9);
                    Entity powerup = null;

                    if (powerUpRandomizer < 5) {
                        if (powerUpRandomizer < 3) { // 1 or 2 out of 8, 1/4 chance of speed boost
                            powerup = PowerupFactory.createPowerup(PowerupType.SPEED_BOOST);
                        } else { // 3 or 4 out of 8, 1/4 chance of health boost
                            powerup = PowerupFactory.createPowerup(PowerupType.HEALTH_BOOST);
                        }
                    }

                    if (powerup == null) {
                        return;
                    }
                    // Hence 5, 6, 7, 8 out of 8, 1/2 chance of nothing
                    ServiceLocator.getStructurePlacementService().spawnEntityAtVector(powerup, enemyBody.cpy());
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
