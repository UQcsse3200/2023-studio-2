package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PowerupFactoryTest {

    @Mock
    PhysicsService physicsService;

    @Mock
    ShapeRenderer shapeRenderer;
    @Mock
    Box2DDebugRenderer physicsRenderer;
    DebugRenderer debugRenderer;
    @BeforeEach
    void setUp() {
        debugRenderer = new DebugRenderer(physicsRenderer, shapeRenderer);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    @Test
    public void testCreateHealthBoostPowerup() {
        // Initialise
        ResourceService resourceService = ServiceLocator.getResourceService();
        String[] imgs = {"images/healthpowerup.png"};
        resourceService.loadTextures(imgs);
        Entity powerup = PowerupFactory.createPowerup(PowerupType.HEALTH_BOOST, 100);
        PowerupComponent powerupComponent = powerup.getComponent(PowerupComponent.class);

        // Check they aren't Null
        assertNotNull(powerup);
        assertNotNull(powerupComponent);

        // Ensure they do as expected
        assertEquals(PowerupType.HEALTH_BOOST, powerupComponent.getType());
        assertEquals(100, powerupComponent.getModifier());
    }
}
