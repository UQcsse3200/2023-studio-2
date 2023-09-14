package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.ships.ShipUpgradesComponent;
import com.csse3200.game.components.ships.ShipUpgradesType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ShipUpgradesConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ShipUpgradesFactory {

    private static final ShipUpgradesConfig configs
            = FileLoader.readClass(ShipUpgradesConfig.class, "configs/shipUpgrades.json");

    public static Entity createUpgrade(ShipUpgradesConfig config) {
        // Initialise and resize a new Powerup
        Entity shipUpgrade = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ShipUpgradesComponent(config.type, PhysicsLayer.SHIP))
                .addComponent(new TextureRenderComponent(config.spritePath));
        shipUpgrade.addComponent(
                new InteractableComponent(
                        shipUpgrade.getComponent(ShipUpgradesComponent.class)::applyUpgrade, 1f));
        shipUpgrade.setScale(0.7f, 0.7f);

        return shipUpgrade;
    }
}
