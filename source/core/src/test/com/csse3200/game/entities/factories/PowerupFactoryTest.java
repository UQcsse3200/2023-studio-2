package com.csse3200.game.entities.factories;

import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.configs.PowerupConfig;
import com.csse3200.game.entities.configs.PowerupConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * PowerupFactoryTest is a testing class that verifies the correct creation
 * and behavior of powerups (like health and speed) created by the PowerupFactory.
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PowerupFactoryTest {

    private static final PowerupConfigs configs =
            FileLoader.readClass(PowerupConfigs .class, "configs/powerups.json");

    @Mock
    ResourceService resourceService;

    /**
     * Set up services and resources necessary for the tests.
     * This method is run before each test method to ensure a fresh context.
     */
    @BeforeEach
    void setUp() {
        resourceService = mock(ResourceService.class);
        when(resourceService.getAsset(Mockito.any(), Mockito.any())).thenReturn(null);
        ServiceLocator.registerResourceService(resourceService);

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        String[] powerupTextures = {"images/healthpowerup.png", "images/speedpowerup.png"};
        resourceService.loadTextures(powerupTextures);
        resourceService.loadAll();
    }

    /**
     * Test the creation of a health powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected health boost type.
     */
    @Test
    public void testCreateHealthPowerup() {
        Entity powerup = PowerupFactory.createHealthPowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.HEALTH_BOOST, type);
    }

    /**
     * Test the creation of a speed powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected speed boost type.
     */
    @Test
    public void testCreateSpeedPowerup() {
        Entity powerup = PowerupFactory.createSpeedPowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.SPEED_BOOST, type);
    }

    @Test
    void createHealthPowerupConfigTest() {
        PowerupConfig config = configs.healthPowerup;
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.HEALTH_BOOST, type);
    }

    @Test
    void createSpeedPowerupConfigTest() {
        PowerupConfig config = configs.speedPowerup;
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.SPEED_BOOST, type);
    }

    /**
     * Test the creation of an extra life powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected extra life type.
     */
    @Test
    public void testCreateExtraLifePowerup() {
        Entity powerup = PowerupFactory.createExtraLifePowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.EXTRA_LIFE, type);
    }

    /**
     * Test the creation of an extra life powerup config.
     * This test verifies that the config created by PowerupConfig is not null
     * and is of the expected extra life type.
     */
    @Test
    void createExtraLifePowerupConfigTest() {
        PowerupConfig config = configs.extraLifePowerup;
        assertNotNull(config);
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.EXTRA_LIFE, type);
    }
    /**
     * Test the creation of an double cross powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected double cross type.
     */
    @Test
    public void testCreateDoubleCrossPowerup() {
        Entity powerup = PowerupFactory.createDoubleCrossPowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.DOUBLE_CROSS, type);
    }

    /**
     * Test the creation of an double cross powerup config.
     * This test verifies that the config created by PowerupConfig is not null
     * and is of the expected double cross type.
     */
    @Test
    void createDoubleCrossPowerupConfigTest() {
        PowerupConfig config = configs.doubleCrossPowerup;
        assertNotNull(config);
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.DOUBLE_CROSS, type);
    }
    /**
     * Test the creation of an double damage powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected double damage type.
     */
    @Test
    public void testCreateDoubleDamagePowerup() {
        Entity powerup = PowerupFactory.createDoubleDamagePowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.DOUBLE_DAMAGE, type);
    }

    /**
     * Test the creation of an double damage powerup config.
     * This test verifies that the config created by PowerupConfig is not null
     * and is of the expected double damage type.
     */
    @Test
    void createDoubleDamagePowerupConfigTest() {
        PowerupConfig config = configs.doubleDamagePowerup;
        assertNotNull(config);
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.DOUBLE_DAMAGE, type);
    }
    /**
     * Test the creation of an snap powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected snap type.
     */
    @Test
    public void testCreateSnapPowerup() {
        Entity powerup = PowerupFactory.createSnapPowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.SNAP, type);
    }

    /**
     * Test the creation of an snap powerup config.
     * This test verifies that the config created by PowerupConfig is not null
     * and is of the expected snap type.
     */
    @Test
    void createSnapPowerupConfigTest() {
        PowerupConfig config = configs.snapPowerup;
        assertNotNull(config);
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.SNAP, type);
    }
    /**
     * Test the creation of an temp immunity powerup entity.
     * This test verifies that the entity created by PowerupFactory is not null
     * and is of the expected temp immunity type.
     */
    @Test
    public void testCreateTempImmunityPowerup() {
        Entity powerup = PowerupFactory.createTempImmunityPowerup();
        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.TEMP_IMMUNITY, type);
    }

    /**
     * Test the creation of an temp immunity powerup config.
     * This test verifies that the config created by PowerupConfig is not null
     * and is of the expected temp immunity type.
     */
    @Test
    void createTempImmunityPowerupConfigTest() {
        PowerupConfig config = configs.tempImmunityPowerup;
        assertNotNull(config);
        Entity powerup = PowerupFactory.createPowerup(config);

        PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

        assertNotNull(powerup);
        assertEquals(PowerupType.TEMP_IMMUNITY, type);
    }
}
