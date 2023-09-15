package com.csse3200.game.components.ships;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

/**
 * Represents a power-up component within the game.
 */
public class ShipUpgradesComponent extends Component {

    private ShipUpgradesType type;

    /**
     * Assigns a type and targetLayer value to a given ShipUpgrade
     */
    public ShipUpgradesComponent(ShipUpgradesType type, short targetLayer) {
        this.type = type;
    }

    /**
     * Applies the effects of the Powerup to the specified target entity.
     *
     * @param ship - the ship to apply the upgrade to
     */
    public void applyUpgrade(Entity ship) {
        ShipActions shipActions = ship.getComponent(ShipActions.class);
        if (shipActions == null) {
            return;
        }

        switch (type) {
            case HEALTH_UPGRADE -> {
                shipActions.setHealth(150);
                shipActions.updatedHealth();
            }

            case FUEL_UPGRADE -> {
                shipActions.setFuel(200);
                shipActions.updatedFuel();
            }

            default -> throw new IllegalArgumentException("You must specify a valid ShipUpgradesType");
        }

        if (entity != null) {
            Gdx.app.postRunnable(entity::dispose);
        }
    }

    /**
     * Retrieves the type of the ShipUpgrades.
     *
     * @return The current ShipUpgrades type.
     */
    public ShipUpgradesType getType() {
        return type;
    }

    /**
     * Sets the type of the ShipUpgrades.
     *
     * @param type  The type to set.
     */
    public void setType(ShipUpgradesType type) {
        this.type = type;
    }
}
