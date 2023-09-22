// package com.csse3200.game.entities.factories;

// import com.csse3200.game.components.PowerupComponent;
// import com.csse3200.game.components.PowerupType;
// import com.csse3200.game.entities.Entity;
// import com.csse3200.game.entities.EntityService;
// import com.csse3200.game.entities.configs.NPCConfigs;
// import com.csse3200.game.entities.configs.PowerupConfig;
// import com.csse3200.game.entities.configs.PowerupConfigs;
// import com.csse3200.game.extensions.GameExtension;
// import com.csse3200.game.files.FileLoader;
// import com.csse3200.game.physics.PhysicsService;
// import com.csse3200.game.services.ResourceService;
// import com.csse3200.game.services.ServiceLocator;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import static org.junit.jupiter.api.Assertions.*;

// /**
//  * PowerupFactoryTest is a testing class that verifies the correct creation
//  * and behavior of powerups (like health and speed) created by the PowerupFactory.
//  */
// @ExtendWith(MockitoExtension.class)
// @ExtendWith(GameExtension.class)
// public class PowerupFactoryTest {

//     private static final PowerupConfigs configs =
//             FileLoader.readClass(PowerupConfigs .class, "configs/powerups.json");


//     /**
//      * Set up services and resources necessary for the tests.
//      * This method is run before each test method to ensure a fresh context.
//      */
//     @BeforeEach
//     void setUp() {
//         ServiceLocator.registerEntityService(new EntityService());
//         ServiceLocator.registerPhysicsService(new PhysicsService());
//         ResourceService resourceService = new ResourceService();
//         ServiceLocator.registerResourceService(resourceService);
//         String[] powerupTextures = {"images/healthpowerup.png", "images/speedpowerup.png", "images/extralifepowerup.png"};
//         resourceService.loadTextures(powerupTextures);
//         resourceService.loadAll();
//     }

//     /**
//      * Test the creation of a health powerup entity.
//      * This test verifies that the entity created by PowerupFactory is not null
//      * and is of the expected health boost type.
//      */
//     @Test
//     public void testCreateHealthPowerup() {
//         Entity powerup = PowerupFactory.createHealthPowerup();
//         PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

//         assertNotNull(powerup);
//         assertEquals(PowerupType.HEALTH_BOOST, type);
//     }

//     /**
//      * Test the creation of a speed powerup entity.
//      * This test verifies that the entity created by PowerupFactory is not null
//      * and is of the expected speed boost type.
//      */
//     @Test
//     public void testCreateSpeedPowerup() {
//         Entity powerup = PowerupFactory.createSpeedPowerup();
//         PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

//         assertNotNull(powerup);
//         assertEquals(PowerupType.SPEED_BOOST, type);
//     }

//     /**
//      * Test the creation of an extra life powerup entity.
//      * This test verifies that the entity created by PowerupFactory is not null
//      * and is of the expected extra life type.
//      */
//     @Test
//     public void testCreateExtraLifePowerup() {
//         Entity powerup = PowerupFactory.createExtraLifePowerup();
//         PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

//         assertNotNull(powerup);
//         assertEquals(PowerupType.EXTRA_LIFE, type);
//     }

//     /**
//      * Test the creation of a health powerup config.
//      * This test verifies that the config created by PowerupConfig is not null
//      * and is of the expected health type.
//      */
//     @Test
//     void createHealthPowerupConfigTest() {
//         PowerupConfig config = configs.healthPowerup;
//         assertNotNull(config);
//         Entity powerup = PowerupFactory.createPowerup(config);

//         PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

//         assertNotNull(powerup);
//         assertEquals(PowerupType.HEALTH_BOOST, type);
//     }

//     /**
//      * Test the creation of a speed powerup config.
//      * This test verifies that the config created by PowerupConfig is not null
//      * and is of the expected speed type.
//      */
//     @Test
//     void createSpeedPowerupConfigTest() {
//         PowerupConfig config = configs.speedPowerup;
//         assertNotNull(config);
//         Entity powerup = PowerupFactory.createPowerup(config);

//         PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

//         assertNotNull(powerup);
//         assertEquals(PowerupType.SPEED_BOOST, type);
//     }

//     /**
//      * Test the creation of an extra life powerup config.
//      * This test verifies that the config created by PowerupConfig is not null
//      * and is of the expected extra life type.
//      */
//     @Test
//     void createExtraLifePowerupConfigTest() {
//         PowerupConfig config = configs.extraLifePowerup;
//         assertNotNull(config);
//         Entity powerup = PowerupFactory.createPowerup(config);

//         PowerupType type = powerup.getComponent(PowerupComponent.class).getType();

//         assertNotNull(powerup);
//         assertEquals(PowerupType.EXTRA_LIFE, type);
//     }
// }
