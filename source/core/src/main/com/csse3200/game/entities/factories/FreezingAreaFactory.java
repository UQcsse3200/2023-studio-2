package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

public class FreezingAreaFactory {

    public static Entity createFreezingArea() {

        Entity freezing = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                .addComponent(new ProximityActivationComponent(5.0f, MapGameArea.getPlayer(), MapGameArea::toggleFreezing, MapGameArea::toggleFreezing));
        freezing.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        freezing.setScale(10.0f, 10.0f);
        System.out.println("freezing created");
        return freezing;
    }


    private FreezingAreaFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
