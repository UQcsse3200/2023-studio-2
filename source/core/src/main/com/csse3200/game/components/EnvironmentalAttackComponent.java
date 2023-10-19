package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.components.HitboxComponent;
import java.util.Timer;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class EnvironmentalAttackComponent extends Component {
    private short targetLayer;

    private HitboxComponent hitboxComponent;
    private boolean leftContact;
    private Timer triggerTimer;
    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public EnvironmentalAttackComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    /**
     * Create a component which attacks entities on collision, with knockback.
     * @param targetLayer The physics layer of the target's collider.
     * @param knockback The magnitude of the knockback applied to the entity.
     */

    /**
     * Creates new listener waiting for projectile entity to interact with another entity
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        leftContact = true;
    }

    /**
     * Runs when projectile collision listener initially makes contact with another entity.
     * Will deal damage to entity upon contact.
     * @param me The current entity's fixture
     * @param other The targeted entity's fixture
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        EnvironmentStatsComponent targetEnvironmentStats = target.getComponent(EnvironmentStatsComponent.class);
        leftContact = false;
        damage(targetEnvironmentStats, targetStats);
    }

    private void damage(EnvironmentStatsComponent targetState, CombatStatsComponent targetStats) {
        if (targetState.getFrozenLevel() < 100) {
            targetState.setFrozenLevel(targetState.getFrozenLevel() + 1);
        }
    }

    /**
     * Will signal when projectile no longer is in contact with target entity
     * @param me The current entity's fixture
     * @param other The targeted entity's fixture
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        // Stop dealing tick damage
        leftContact = true;
    }
}