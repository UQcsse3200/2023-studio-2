package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Represents a power-up component within the game.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private short targetLayer;
    private CombatStatsComponent companionCombatStats;
    private HitboxComponent hitboxComponent;
    private CompanionActions companionActions;
    private long duration;

    /**
     * Assigns a type and modified value to a given Powerup
     */
    public PowerupComponent(PowerupType type, short targetLayer) {
        this.type = type;
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        companionCombatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    /**
     * Called when a collision begins. Determines if the Powerup should be applied.
     *
     * @param me     The fixture representing this Powerup.
     * @param other  The fixture this Powerup collided with.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            applyEffect(target);
        }
    }

    /**
     * Applies the effects of the Powerup to the specified target entity.
     *
     * @param target  The entity receiving the Powerup effect.
     */
    private void applyEffect(Entity target) {
        companionActions = target.getComponent(CompanionActions.class);

        switch (type) {

            case SPEED_BOOST:
                CompanionActions.setSpeed(5,5);
                this.setDuration(1500);

                // Speed up for 1.5 seconds, then return to normal speed
                java.util.TimerTask speedUp = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        CompanionActions.setSpeed(3, 3);
                    }
                };
                new java.util.Timer().schedule(speedUp, getDuration());
                break;
            default:
                throw new IllegalArgumentException("You must specify a valid PowerupType");
        }
        ServiceLocator.getEntityService().unregister(entity);
        // entity.dispose();
        Gdx.app.postRunnable(entity::dispose);
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
