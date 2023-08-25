package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;


/**
 * Represents a power-up component within the game. This will be extended
 * further to specify the type of power-up or its effects.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private short targetLayer;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;

    /**
     * Assigns a type and modifier value to a given powerup
     */
    public PowerupComponent(PowerupType type, short targetLayer) {
        this.type = type;
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

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

    // Applies an effect to a given entity, removes powerup from game on pickup
    public void applyEffect(Entity target) {
        CombatStatsComponent playerStats =
                target.getComponent(CombatStatsComponent.class);
        switch (type) {
            case HEALTH_BOOST:
                playerStats.setHealth(100);
                break;
        }
        ForestGameArea.removeEntity(entity);
    }

    public PowerupType getType() {
        return type;
    }

    public void setType(PowerupType type) {
        this.type = type;
    }
}

