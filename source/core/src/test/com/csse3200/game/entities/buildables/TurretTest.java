package com.csse3200.game.entities.buildables;

import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.configs.TurretConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class TurretTest {

    private Turret turret;

    @Mock
    ResourceService resourceService;

    @Before
    public void setUp() {
        ServiceLocator.registerResourceService(resourceService);

        TurretConfig turretConfig = new TurretConfig();
        turretConfig.maxAmmo = 10;
        turret = new Turret(turretConfig);
    }



    @Test
    public void testRefillAmmo() {
        Assertions.assertEquals(10, turret.currentAmmo);

        // Refill ammo to max
        turret.refillAmmo();
        Assertions.assertEquals(10, turret.currentAmmo);
    }



    @Test
    public void testCanFire() {
        Assertions.assertTrue(turret.Canfire());


        turret.currentAmmo = 0;
        Assertions.assertFalse(turret.Canfire());


        turret.refillAmmo();
        Assertions.assertTrue(turret.Canfire());
    }


    @Test
    public void testRefillAmmoWithAmount() {

        Assertions.assertEquals(10, turret.currentAmmo);


        turret.refillAmmo(3);
        Assertions.assertEquals(10, turret.currentAmmo);


        turret.currentAmmo = 0;
        turret.refillAmmo(5);
        Assertions.assertEquals(5, turret.currentAmmo);
    }

}

