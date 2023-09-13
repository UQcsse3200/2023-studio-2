package com.csse3200.game.entities.buildables;

import com.csse3200.game.entities.buildables.Turret;
import com.csse3200.game.entities.configs.TurretConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurretTest {

    private Turret turret;

    @Before
    public void setUp() {
        TurretConfig turretConfig = new TurretConfig();
        turretConfig.setMaxAmmo(10); // Use setters if available
        turretConfig.setDamage(5); // Use setters if available
        turret = new Turret(turretConfig);
    }



    public void testRefillAmmo() {
        assertEquals(10, turret.currentAmmo);

        // Refill ammo to max
        turret.refillAmmo();
        assertEquals(10, turret.currentAmmo);
    }


    public void testCanFire() {
        assertTrue(turret.Canfire());


        turret.currentAmmo = 0;
        assertFalse(turret.Canfire());


        turret.refillAmmo();
        assertTrue(turret.Canfire());
    }


    public void testRefillAmmoWithAmount() {

        assertEquals(10, turret.currentAmmo);


        turret.refillAmmo(3);
        assertEquals(10, turret.currentAmmo); // Should not exceed max ammo


        turret.currentAmmo = 0;
        turret.refillAmmo(5);
        assertEquals(5, turret.currentAmmo);
    }






}

