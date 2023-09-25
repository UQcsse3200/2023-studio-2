/**
 * Component representing a power-up (potion) within the game.
 * It provides methods for applying various effects to entities when consumed.
 */
package com.csse3200.game.components;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.components.Companion.CompanionStatsDisplay;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;


/**
 * PotionComponent represents a power-up (potion) in the game, with the ability to apply
 * different effects to entities when consumed.
 */
public class PotionComponent extends Component {

    private PotionType type;
    private long duration;
    private Entity player = ServiceLocator.getEntityService().getPlayer();
    private Entity companion = ServiceLocator.getEntityService().getCompanion();
    private PlayerActions playerActions;
    private CompanionActions companionActions;
     /**
     * Creates a PotionComponent with the specified type.
     *
     * @param type The type of the potion.
     */
    public PotionComponent(PotionType type) {
        this.type = type;
    }

    /**
     * Overrides the Component's create() function.
     */
    @Override
    public void create() {}

    /**
     * Applies the effects of the potion to the specified target entities.
     */
    public void applyEffect() {
        playerActions = player.getComponent(PlayerActions.class);
        companionActions = companion.getComponent(CompanionActions.class);

        switch (type) {
            case DEATH_POTION -> {
               /* enemy.dispose();*/
            }
            case HEALTH_POTION -> {
                player.getComponent(CombatStatsComponent.class).setHealth(100);
                companion.getComponent(CombatStatsComponent.class).setHealth(50);

            }
            case SPEED_POTION -> {
                if (playerActions == null||companionActions==null) {
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
            case INVINCIBILITY_POTION -> {
                if (playerActions == null||companionActions==null) {
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
            case EXTRA_LIFE -> player.getComponent(CombatStatsComponent.class).addLife();
            case DOUBLE_DAMAGE -> {

            }
            default -> throw new IllegalArgumentException("Invalid PotionType");
        }
    }

    /**
     * Sets the duration for which the potion effect should last.
     *
     * @param duration Duration in milliseconds.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the duration for which the potion effect should last.
     *
     * @return Duration in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Retrieves the type of the potion.
     *
     * @return The current potion type.
     */
    public PotionType getType() {
        return type;
    }

    /**
     * Sets the type of the potion.
     *
     * @param type The type to set.
     */
    public void setType(PotionType type) {
        this.type = type;
    }
}
