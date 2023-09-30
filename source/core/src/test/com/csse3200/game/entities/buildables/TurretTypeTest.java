package com.csse3200.game.entities.buildables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test the TurretType enum.
 */
public class TurretTypeTest {

    @Test
    public void values() {
        assertEquals(2, TurretType.values().length);
        assertEquals(TurretType.LEVEL_ONE, TurretType.values()[0]);
    }

    /**
     * Test method for {@link com.csse3200.game.entities.buildables.TurretType#valueOf(java.lang.String)}.
     */
    @Test
    public void valueOf() {
        assertEquals(TurretType.LEVEL_ONE, TurretType.valueOf("LEVEL_ONE"));
        assertEquals(TurretType.LEVEL_TWO, TurretType.valueOf("LEVEL_TWO"));
    }
}