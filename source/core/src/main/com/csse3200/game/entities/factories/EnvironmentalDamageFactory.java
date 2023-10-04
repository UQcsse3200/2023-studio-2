package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.EnvironmentalAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

public class EnvironmentalDamageFactory {

    public static Entity createDamage() {

        Entity damage = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                .addComponent(new EnvironmentalAttackComponent(PhysicsLayer.PLAYER ));
        damage.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        damage.setScale(90.0f, 90.0f);
        return damage;
    }


    private EnvironmentalDamageFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
