package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

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
    public PowerupComponent(PowerupType type) {
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
     * @param target The entity receiving the Powerup effect.
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
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        playerActions.setSpeed(3, 3);
                    }
                }, getDuration());
            }
            case EXTRA_LIFE -> playerCombatStats.addLife();
            case TEMP_IMMUNITY -> {

                if (playerActions == null) {
                    return;
                }

                playerCombatStats.setImmunity(true);
                this.setDuration(6000);
                java.util.TimerTask speedUp = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        playerCombatStats.setImmunity(false);
                    }
                };
                new java.util.Timer().schedule(speedUp, getDuration());
            }
            case DOUBLE_DAMAGE -> {
                if (playerActions == null) {
                    return;
                }

                playerCombatStats.setAttackMultiplier(2);
                this.setDuration(12000);
                java.util.TimerTask speedUp = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        playerCombatStats.setAttackMultiplier(1);
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
     * @param duration Duration in milliseconds.
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
     * @param type The type to set.
     */
    public void setType(PowerupType type) {
        this.type = type;
    }
}
