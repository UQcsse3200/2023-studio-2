package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TurretConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class TurretTest {

    private Turret turret;

    @Mock
    ResourceService resourceService;

    @Mock
    PhysicsService physicsService;

    @Mock
    RenderService renderService;

    @BeforeEach
    public void setUp() {
        TurretConfig turretConfig = new TurretConfig();
        turretConfig.setMaxAmmo(10);

        resourceService = mock(ResourceService.class);
        when(resourceService.getAsset(turretConfig.spritePath, TextureAtlas.class)).thenReturn(mock(TextureAtlas.class));
        ServiceLocator.registerResourceService(resourceService);

        physicsService = mock(PhysicsService.class);
        when(physicsService.getPhysics()).thenReturn(new PhysicsEngine());
        ServiceLocator.registerPhysicsService(physicsService);

        renderService = mock(RenderService.class);
        ServiceLocator.registerRenderService(renderService);

        turret = new Turret(turretConfig);
    }

    @Test
    public void testRefillAmmo() {
        Assertions.assertEquals(10, turret.getCurrentAmmo());

        // Refill ammo to max
        turret.refillAmmo();
        Assertions.assertEquals(10, turret.getCurrentAmmo());
    }



    @Test
    public void testCanFire() {
        Assertions.assertTrue(turret.canFire());


        turret.setCurrentAmmo(0);
        Assertions.assertFalse(turret.canFire());


        turret.refillAmmo();
        Assertions.assertTrue(turret.canFire());
    }


    @Test
    public void testRefillAmmoWithAmount() {

        Assertions.assertEquals(10, turret.getCurrentAmmo());


        turret.refillAmmo(3);
        Assertions.assertEquals(10, turret.getCurrentAmmo());


        turret.setCurrentAmmo(0);
        turret.refillAmmo(5);
        Assertions.assertEquals(5, turret.getCurrentAmmo());
    }

}

