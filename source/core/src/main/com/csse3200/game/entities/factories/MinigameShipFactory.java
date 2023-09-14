package com.csse3200.game.entities.factories;

import com.csse3200.game.components.ships.ShipActions;
import com.csse3200.game.components.ships.ShipStatDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.MinigameConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class MinigameShipFactory {
    private static final MinigameConfigs minigameConfigs =
            FileLoader.readClass(MinigameConfigs.class, "configs/minigame.json");

    //TODO: REMOVE - LEGACY
    public static Entity createMinigameShip() {
        return createMinigameShip(minigameConfigs.ship);
    }

    /**
     * Creates a new minigame ship to match the config file
     * @param config Configuration file to match ship to
     * @return Created minigame ship
     */
    public static Entity createMinigameShip(BaseEntityConfig config) {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForShip();

        Entity ship =
                new Entity().addComponent(new TextureRenderComponent("images/Ship.png"))
                        //.addComponent(new TextureRenderComponent("images/LeftShip.png"))Dont add 2 of the same component class
                        .addComponent(new ShipStatDisplay())
                        .addComponent(new TextureRenderComponent(config.spritePath))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SHIP))
                        .addComponent(new ShipActions())
                        .addComponent(inputComponent);

        PhysicsUtils.setScaledCollider(ship, 0.6f, 0.3f);
        ship.getComponent(ColliderComponent.class).setDensity(1.5f);
        ship.getComponent(TextureRenderComponent.class).scaleEntity();
        return ship;
    }

    private MinigameShipFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}


