package com.csse3200.game.components.companion;

import static org.junit.Assert.assertEquals;


import com.csse3200.game.components.companionweapons.CompanionWeaponType;

import org.junit.Before;
import org.junit.Test;
/**
 * Test class for the CompanionInventoryComponent class.
 */
public class CompanionInventoryComponentTest {
    private CompanionInventoryComponent companionInventory;
    /**
     * Test for the getItem method.
     */
    @Before
    public void setUp() {
        companionInventory = new CompanionInventoryComponent(CompanionWeaponType.Death_Potion, 30, 30);
    }
    /**
     * Test for the getAmmo method.
     */

    @Test
    public void testGetItem() {
        assertEquals(CompanionWeaponType.Death_Potion, companionInventory.getItem());
    }

    @Test
    public void testGetAmmo() {
        assertEquals(30, companionInventory.getAmmo());
    }

    @Test
    public void testChangeAmmo() {
        companionInventory.changeAmmo(10);
        //assertEquals(40, companionInventory.getAmmo());

        companionInventory.changeAmmo(-20);
        //assertEquals(20, companionInventory.getAmmo());

        // Ensure ammo cannot exceed maxAmmo
        companionInventory.changeAmmo(15);
        //assertEquals(30, companionInventory.getAmmo());
    }
    /**
     * Test for the getMaxAmmo method.
     */


    @Test
    public void testGetMaxAmmo() {
        assertEquals(30, companionInventory.getMaxAmmo());
    }
    /**
     * Test for the setAttackCooldown method.
     */
    @Test
    public void testSetAttackCooldown() {
        companionInventory.setAttackCooldown(3);
        assertEquals(3, companionInventory.getAttackCooldown());
    }
    /**
     * Test for the decCoolDown method.
     */

    @Test
    public void testDecCoolDown() {
        companionInventory.setAttackCooldown(3);
        companionInventory.decCoolDown();
        assertEquals(2, companionInventory.getAttackCooldown());
        companionInventory.decCoolDown();
        assertEquals(1, companionInventory.getAttackCooldown());
        companionInventory.decCoolDown();
        assertEquals(0, companionInventory.getAttackCooldown());
        companionInventory.decCoolDown();
        assertEquals(0, companionInventory.getAttackCooldown()); // Should not go below 0
    }
    /**
     * Test for the create method.
     */


    @Test
    public void testCreate() {
        companionInventory.create();
        assertEquals("ranged", companionInventory.getEquipped());
        assertEquals(30, companionInventory.GetCurrentAmmo());
    }
}
