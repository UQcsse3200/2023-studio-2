package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.ships.ShipUpgradesComponent;
import com.csse3200.game.components.ships.ShipUpgradesType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ShipUpgradesConfig;
import com.csse3200.game.entities.configs.AllShipUpgradesConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ShipUpgradesFactory {

    private static final AllShipUpgradesConfig configs
            = FileLoader.readClass(AllShipUpgradesConfig.class, "configs/shipUpgrades.json");

    /**
     * Based on given ShipUpgradesConfig, create the relevant stat upgrade
     * @param config
     * @return the relevant stat ShipUpgrade entity
     */
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

    /**
     * Create the health upgrade for the ship
     * @return the Health stat upgrade entity
     */
    public static Entity createHealthUpgrade() {return createUpgrade(configs.healthUpgrade);}

    /**
     * Create the fuel upgrade for the ship
     * @return the fuel stat upgrade entity
     */
    public static Entity createFuelUpgrade() {return createUpgrade(configs.fuelUpgrade);}


 }
