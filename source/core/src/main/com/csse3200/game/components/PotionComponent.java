package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.Companion.CompanionInventoryComponent;
import com.csse3200.game.components.LaboratoryInventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;



/**
 * Represents a power-up component within the game.
 */
public class PotionComponent extends Component {

    private PotionType type;
    private long duration;
    private CombatStatsComponent playerCombatStats;
    private CombatStatsComponent companionCombatStats;
    private PlayerActions playerActions;
    private CompanionActions companionActions;

    /**
     * Assigns a type and targetLayer value to a given Potion
     */
    public PotionComponent(PotionType type) {
        this.type = type;
    }

    /**
     * Overrides the Component create() function
     */
    @Override
    public void create() {
        playerCombatStats = entity.getComponent(CombatStatsComponent.class);
        companionCombatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Applies the effects of the Potion to the specified target entity.
     *
     * @param target1  The entity receiving the Potion effect.
     */
    public void applyEffect(Entity target1,Entity target2) {
        playerCombatStats = target1.getComponent(CombatStatsComponent.class);
        playerActions = target1.getComponent(PlayerActions.class);
        companionCombatStats = target2.getComponent(CombatStatsComponent.class);
        companionActions = target2.getComponent(CompanionActions.class);

        switch (type){
            case DEATH_POTION -> {
                return;
            }
            case HEALTH_POTION -> {if (playerActions == null&&companionActions== null) {return;}
                int currentHealthPlayer = playerCombatStats.getHealth();
                playerCombatStats.setHealth(100);
                int currentHealthCompanion = companionCombatStats.getHealth();
                playerCombatStats.setHealth(100);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        playerCombatStats.setHealth(currentHealthPlayer);
                        companionCombatStats.setHealth(currentHealthCompanion);
                    }
                }, 3.0f);
            }
            case SPEED_POTION -> {if (playerActions == null&&companionActions== null) {return;}
                playerActions.setSpeed(6,6);
                companionActions.setSpeed(7,7);
                this.setDuration(10000);
                java.util.TimerTask speedUp = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        playerActions.setSpeed(3, 3);
                        companionActions.setSpeed(4,4);
                    }
                };
                new java.util.Timer().schedule(speedUp, getDuration());

            }
            default -> throw new IllegalArgumentException("You must specify a valid PotionType");
        }
    }

    /**
     * Sets the duration for which the Potion effect should last.
     *
     * @param duration  Duration in milliseconds.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the duration for which the Potion effect should last.
     *
     * @return Duration in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Retrieves the type of the Potion.
     *
     * @return The current Potion type.
     */
    public PotionType getType() {
        return type;
    }

    /**
     * Sets the type of the Potion.
     *
     * @param type  The type to set.
     */
    public void setType(PotionType type) {
        this.type = type;
    }
}