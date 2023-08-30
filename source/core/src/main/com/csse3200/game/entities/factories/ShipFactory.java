package com.csse3200.game.entities.factories;

import com.csse3200.game.components.ships.ShipActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class ShipFactory {
    //private static final ShipConfig stats =
     //       FileLoader.readClass(ShipConfig.class, "configs/ship.json");

    public static Entity createShip() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForShip();

        Entity ship =
                new Entity()

                        .addComponent(new TextureRenderComponent("images/Ship.png"))
                        //.addComponent(new TextureRenderComponent("images/LeftShip.png"))Dont add 2 of the same component class

                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SHIP))
                        .addComponent(new ShipActions())
                        //.addComponent(new ShipStatsComponent(stats.health))
                        //.addComponent(new InventoryComponent(stats.gold))
                        .addComponent(inputComponent);
                        //.addComponent(new PlayerStatsDisplay());

        PhysicsUtils.setScaledCollider(ship, 0.6f, 0.3f);
        ship.getComponent(ColliderComponent.class).setDensity(1.5f);
        ship.getComponent(TextureRenderComponent.class).scaleEntity();
        return ship;
    }

    private ShipFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}


