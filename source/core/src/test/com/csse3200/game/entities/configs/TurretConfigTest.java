package com.csse3200.game.entities.configs;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This class is used to test the TurretConfig class.
 */
public class TurretConfigTest {
    @Test
    public void testTurretConfig() {
        TurretConfig turretConfig = new TurretConfig();
        assertNotEquals(String.valueOf(0), turretConfig.health, null); // Test for null
        assertEquals(String.valueOf(0), turretConfig.maxAmmo, 0); // Test for 0
        assertEquals(0, turretConfig.damage);
        assertEquals(1, turretConfig.attackMultiplier);
        assertFalse(turretConfig.isImmune);
    }

}