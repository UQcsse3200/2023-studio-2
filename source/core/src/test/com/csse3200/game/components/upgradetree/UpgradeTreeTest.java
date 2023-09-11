package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpgradeTreeTest {

    private UpgradeTree upgradeTree;

    @BeforeEach
    public void setUp() {
        upgradeTree = new UpgradeTree();
    }

    @Test
    public void testGetUnlockedWeapons() {
        assertEquals(3, upgradeTree.getUnlockedWeapons().size());
    }

    @Test
    public void testDefaultWeapons() {
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.WOODHAMMER));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.THROW_ELEC_WRENCH));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STICK));
    }

    @Test
    public void testUnlockWeapon() {
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.LASERGUN));
        upgradeTree.unlockWeapon(WeaponType.LASERGUN);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.LASERGUN));
    }

    @Test
    public void testGetStartingMaterials() {
        int materials  = upgradeTree.getMaterials();
        assertEquals(0, materials);
    }

    // todo: write testSubtractMaterials based on getting extractor resources
}
