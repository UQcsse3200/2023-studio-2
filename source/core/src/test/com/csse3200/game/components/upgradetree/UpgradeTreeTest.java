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
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.SLING_SHOT));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STICK));
    }

    @Test
    public void testUnlockWeapon() {
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER));
        upgradeTree.unlockWeapon(WeaponType.STONEHAMMER);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER));
    }

    @Test
    public void testGetStartingMaterials() {
        int materials  = upgradeTree.getMaterials();
        assertEquals(0, materials);
    }

    // todo: write testSubtractMaterials based on getting extractor resources
}
