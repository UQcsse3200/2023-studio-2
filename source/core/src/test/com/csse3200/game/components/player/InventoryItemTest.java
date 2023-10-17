package com.csse3200.game.components.player;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;

public class InventoryItemTest {
    private WeaponConfigs weaponConfigs;
    private InventoryComponent.InventoryItem inventoryItem;

    @Before
    public void setUp() {
        weaponConfigs = FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
        inventoryItem = new InventoryComponent(weaponConfigs).new InventoryItem(WeaponType.MELEE_WRENCH, 30, 40);
    }

    @Test
    public void testGetItem() {
        assertEquals(WeaponType.MELEE_WRENCH, inventoryItem.getItem());
    }

    @Test
    public void testChangeItem() {
        inventoryItem.changeItem(WeaponType.RANGED_SLINGSHOT);
        assertEquals(WeaponType.RANGED_SLINGSHOT, inventoryItem.getItem());
    }

    @Test
    public void testGetAmmo() {
        assertEquals(30, inventoryItem.getAmmo());
    }

    @Test
    public void testChangeAmmo() {
        inventoryItem.changeAmmo(10);
        assertEquals(40, inventoryItem.getAmmo());

        inventoryItem.changeAmmo(-15);
        assertEquals(25, inventoryItem.getAmmo());
    }

    @Test
    public void testSetMaxAmmo() {
        inventoryItem.setMaxAmmo();
        assertEquals(40, inventoryItem.getAmmo());
    }

    @Test
    public void testGetMaxAmmo() {
        assertEquals(40, inventoryItem.getMaxAmmo());
    }

    @Test
    public void testSetAttackCooldown() {
        inventoryItem.setAttackCooldown(5);
        assertEquals(5, inventoryItem.getAttackCooldown());
    }

    @Test
    public void testGetAttackCooldown() {
        assertEquals(0, inventoryItem.getAttackCooldown());
    }

    @Test
    public void testDecCoolDown() {
        inventoryItem.setAttackCooldown(3);
        inventoryItem.decCoolDown();
        assertEquals(2, inventoryItem.getAttackCooldown());
    }

}
