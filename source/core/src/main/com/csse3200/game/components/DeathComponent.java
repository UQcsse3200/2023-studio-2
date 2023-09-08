package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * When this entity (usually killable entities) has health = 0, it disposes the enemy form the field of play.
 */
public class DeathComponent extends Component {
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;

    /**
     * The Death Component holding Physical Interaction Stats, and facilitates listeners for entity death
     */
    public DeathComponent() {
    }

    /**
     * Creates a new listener on an entity to wait for kill condition
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionEnd", this::kill);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    /**
     * When kill condition met, target entity will be disposed.
     * @param me The current Entity's Fixture
     * @param other The targeted Entity's Fixture
     */
    private void kill(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }
        if (combatStats.isDead()) {
            AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
            animator.stopAnimation();
            entity.getComponent(HitboxComponent.class).setLayer((short) 0);

            entity.getEvents().trigger("dispose");
            // Schedule a task to execute entity::dispose after a delay
            // Get the duration of the death animation
            float deathAnimationDuration = animator.getAnimationDuration("death");
            // Convert the duration from seconds to milliseconds for the Timer
            long delay = (long) (deathAnimationDuration * 1000);
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(entity::dispose);
                }
            }, delay); // Delay based on the death animation duration
        }
    }
}