package com.csse3200.game.entities.configs;

import com.csse3200.game.components.ships.ShipUpgradesType;

public class ShipUpgradesConfig extends BaseEntityConfig {
    public ShipUpgradesType type;

    public ShipUpgradesConfig() {
        this.spritePath = "images/LeftShip.png";
    }
}