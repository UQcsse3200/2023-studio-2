package com.csse3200.game.components.ships;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.ui.DialogComponent;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.ships.ShipActions;
import com.csse3200.game.components.Component;
//reference com.csse3200.game.components.TouchAttackComponent;

/**
 * SpaceCollideDamageComponent is responsible for dealing damage to entities in the space obstacle
 * minigame when this entity collides with the Ship entity's hitbox
 *
 * <p>This component requires the presence HitboxComponent on this entity.
 *
 * <p>Damage is only applied if the target entity has a ShipActions component. Knockback is only applied
 * if the target entity has a PhysicsComponent.
 */
public class SpaceCollideDamageComponent extends Component {

    private short targetLayer;
    private float knockbackForce = 1f;
    private HitboxComponent hitboxComponent;
    private boolean leftContact;

    /**
     * Creates a SpaceCollideDamageComponent that deals damage on target entity (Ship) on collision,
     * with knockback
     * @param targetLayer The physics layer of the target's hitbox.
     * @param knockBack The magnitude of the knockback applied to the entity.
     */
    public SpaceCollideDamageComponent(short targetLayer, float knockBack) {
        this.targetLayer = targetLayer;
        this.knockbackForce = knockBack;
    }

    /**
     * Creates listener that checks if current entity and a target entity come into contact.
     */
    public void create() {
        // Listen for collision events
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

        // Retrieve necessary components
        hitboxComponent = entity.getComponent(HitboxComponent.class);

        leftContact = true;
    }

    /**
     * Initial collision between current entity and target entity.
     * Deals single instance of damage when hit by enemy.
     * @param me The current entity as a Fixture
     * @param other The Target entity as a Fixture
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
        // Has come into contact

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        Entity source = ((BodyUserData) me.getBody().getUserData()).entity;

        ShipActions targetStats = target.getComponent(ShipActions.class);
        //CombatStatsComponent sourceStats = source.getComponent(CombatStatsComponent.class);
        leftContact = false;
        // If No Hitbox
        if (target.getComponent(HitboxComponent.class) == null) {
            return;
        }
        //Will probably need to check for entities of enemy and other layer types
        hitOnce(target, source, targetStats);
    }

    /**
     * Helper Method that deals damage and knockback to Target
     * @param target The Targeted Entity, usually the Ship Entity
     * @param source The current Entity
     * @param targetStats The targeted Entity's stats contained in ShipActions, must be Ship entity
     */
    private void hitOnce(Entity target, Entity source, ShipActions targetStats) {
        if (targetStats != null) {

            //deals damage to target
            targetStats.hit();

            // Apply knockback
            PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
            if (physicsComponent != null && knockbackForce > 0f) {
                Body targetBody = physicsComponent.getBody();
                Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
                Vector2 impulse = direction.setLength(knockbackForce);
                targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
            }
        }
    }

    /**
     * Target has left contact with current object
     * @param me current object
     * @param other target object
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        // Stop dealing tick damage
        leftContact = true;
    }
}