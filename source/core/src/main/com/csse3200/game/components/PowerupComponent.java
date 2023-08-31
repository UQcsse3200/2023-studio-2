package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;

/**
 * Represents a power-up component within the game.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private CombatStatsComponent playerCombatStats;
    private PlayerActions playerActions;
    private long duration;

    /**
     * Assigns a type and targetLayer value to a given Powerup
     */
    public PowerupComponent(PowerupType type, short targetLayer) {
        this.type = type;
    }

    /**
     * Overrides the Component create() function
     */
    @Override
    public void create() {
        playerCombatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Applies the effects of the Powerup to the specified target entity.
     *
     * @param target  The entity receiving the Powerup effect.
     */
    public void applyEffect(Entity target) {
        playerCombatStats = target.getComponent(CombatStatsComponent.class);
        playerActions = target.getComponent(PlayerActions.class);

        switch (type) {
            case HEALTH_BOOST -> playerCombatStats.setHealth(100);
            case SPEED_BOOST -> {

                if (playerActions == null) {
                    return;
                }

                playerActions.setSpeed(5, 5);
                this.setDuration(1500);

                // Speed up for 1.5 seconds, then return to normal speed
                java.util.TimerTask speedUp = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        playerActions.setSpeed(3, 3);
                    }
                };
                new java.util.Timer().schedule(speedUp, getDuration());
            }
            default -> throw new IllegalArgumentException("You must specify a valid PowerupType");
        }

        if (entity != null) {
            Gdx.app.postRunnable(entity::dispose);
        }
    }

    /**
     * Sets the duration for which the Powerup effect should last.
     *
     * @param duration  Duration in milliseconds.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the duration for which the Powerup effect should last.
     *
     * @return Duration in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Retrieves the type of the Powerup.
     *
     * @return The current Powerup type.
     */
    public PowerupType getType() {
        return type;
    }

    /**
     * Sets the type of the Powerup.
     *
     * @param type  The type to set.
     */
    public void setType(PowerupType type) {
        this.type = type;
    }
}

