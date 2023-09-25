package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * Represents a power-up component within the game.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private Entity player = ServiceLocator.getEntityService().getPlayer();
    private Entity companion = ServiceLocator.getEntityService().getCompanion();
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
     *
     *
     * @param target The entity receiving the Powerup effect.
     */
    public void applyEffect(Entity target) {
        playerCombatStats = target.getComponent(CombatStatsComponent.class);
        playerActions = target.getComponent(PlayerActions.class);

        switch (type) {
            case HEALTH_BOOST ->{
                playerCombatStats.setHealth(100);
                companion.getComponent(CombatStatsComponent.class).setHealth(50);
                entity.getEvents().trigger("playSound", "healthPowerup"); //plays sound when health powerup selected
            }


            case SPEED_BOOST -> {

                if (playerActions == null) {
                    return;
                }
                player.getComponent(PlayerActions.class).setSpeed(6,6);
                companion.getComponent(CompanionActions.class).setSpeed(7,7);
                companion.getComponent(FollowComponent.class).setFollowSpeed(5);

                // Set the duration for speed effect
                this.setDuration(10000);

                // Schedule a task to reset the speed values after the specified duration
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.getComponent(PlayerActions.class).setSpeed(3,3);
                        companion.getComponent(CompanionActions.class).setSpeed(4,4);
                    }
                },getDuration());
            }
            case EXTRA_LIFE -> playerCombatStats.addLife();

            case TEMP_IMMUNITY -> {
                if (playerActions == null) {
                    return;
                }
                companion.getComponent(CombatStatsComponent.class).setImmunity(true);
                player.getComponent(CombatStatsComponent.class).setImmunity(true);
                this.setDuration(8000);
                Timer.schedule(new Timer.Task()  {
                    @Override
                    public void run() {
                        companion.getComponent(CombatStatsComponent.class).setImmunity(false);
                        player.getComponent(CombatStatsComponent.class).setImmunity(false);

                    }
                },getDuration());
            }
            case DOUBLE_DAMAGE -> {
                if (playerActions == null) {
                    return;
                }

                playerCombatStats.setAttackMultiplier(2);
                this.setDuration(12000);

                Timer.Task doubleDamage = new Timer.Task() {
                    @Override
                    public void run() {
                        playerCombatStats.setAttackMultiplier(1);
                    }
                };

                new Timer().scheduleTask(doubleDamage, getDuration());
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
