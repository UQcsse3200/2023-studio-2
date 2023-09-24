/**
 * Component representing a power-up (potion) within the game.
 * It provides methods for applying various effects to entities when consumed.
 */
package com.csse3200.game.components;

import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;


/**
 * PotionComponent represents a power-up (potion) in the game, with the ability to apply
 * different effects to entities when consumed.
 */
public class PotionComponent extends Component {

    private PotionType type;
    private long duration;
    private CombatStatsComponent playerCombatStats;
    private CombatStatsComponent companionCombatStats;
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
    public void create() {
        playerCombatStats = entity.getComponent(CombatStatsComponent.class);
        companionCombatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Applies the effects of the potion to the specified target entities.
     *
     * @param target1 The first entity receiving the potion effect.
     * @param target2 The second entity receiving the potion effect.
     */
    public void applyEffect(Entity target1, Entity target2) {
        playerCombatStats = target1.getComponent(CombatStatsComponent.class);
        playerActions = target1.getComponent(PlayerActions.class);
        companionCombatStats = target2.getComponent(CombatStatsComponent.class);
        companionActions = target2.getComponent(CompanionActions.class);

        switch (type) {
            case DEATH_POTION -> {
                // Handle death potion effect
                // Currently, no action is taken for this type
                return;
            }
            case HEALTH_POTION -> {
                if (playerActions == null && companionActions == null) {
                    return;
                }
                // Store current health values
                int currentHealthPlayer = playerCombatStats.getHealth();
                int currentHealthCompanion = companionCombatStats.getHealth();

                // Set health to a specific value temporarily
                playerCombatStats.setHealth(100);
                companionCombatStats.setHealth(50);

            }
            case SPEED_POTION -> {
                if (playerActions == null && companionActions == null) {
                    return;
                }
                // Adjust the speed of player and companion
                playerActions.setSpeed(6, 6);
                companionActions.setSpeed(7, 7);

                // Set the duration for speed effect
                this.setDuration(10000);

                // Schedule a task to reset the speed values after the specified duration
                java.util.TimerTask speedUp = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        playerActions.setSpeed(3, 3);
                        companionActions.setSpeed(4, 4);
                    }
                };
                new java.util.Timer().schedule(speedUp, getDuration());
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
