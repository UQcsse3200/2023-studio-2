package com.csse3200.game.entities.factories;

import com.csse3200.game.components.ships.ShipUpgradesComponent;
import com.csse3200.game.components.ships.ShipUpgradesType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.AllShipUpgradesConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ShipUpgradesFactoryTest is a testing class that verifies the correct creation
 * and behavior of powerups (like health and speed) created by the PowerupFactory.
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class ShipUpgradesFactoryTest {

    private static final AllShipUpgradesConfig configs =
            FileLoader.readClass(AllShipUpgradesConfig.class, "configs/shipUpgrades.json");

    /**
     * Set up services and resources necessary for the tests.
     * This method is run before each test method to ensure a fresh context.
     */
    @BeforeEach
    void setUp() {
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        String[] shipUpgradesTextures = {"images/powerups/health_boost.png"};
        String[] fuelUpgradesTextures = {"images/powerups/speed_boost.png"};
        resourceService.loadTextures(shipUpgradesTextures);
        resourceService.loadTextures(fuelUpgradesTextures);
        resourceService.loadAll();
    }

    /**
     * Test the creation of a health upgrade entity that is currently the default upgrade in ShipConfig.
     * This test verifies that the entity created by ShipUpgradesFactory is not null
     * and is of the expected HEALTH_UPGRADE type.
     */
    @Test
    public void testCreateUpgrade() {
        Entity shipUpgrade = ShipUpgradesFactory.createHealthUpgrade();
        Entity fuelUpgrade = ShipUpgradesFactory.createFuelUpgrade();
        ShipUpgradesType type = shipUpgrade.getComponent(ShipUpgradesComponent.class).getType();
        ShipUpgradesType fuel = fuelUpgrade.getComponent(ShipUpgradesComponent.class).getType();
        assertNotNull(shipUpgrade);

        assertEquals(type, ShipUpgradesType.HEALTH_UPGRADE);
        assertEquals(fuel, ShipUpgradesType.FUEL_UPGRADE);
    }

}

