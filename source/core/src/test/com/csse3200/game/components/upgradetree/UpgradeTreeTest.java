package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class UpgradeTreeTest {

    private UpgradeTree upgradeTree;
    GameStateObserver gameStateObserver;
    String resourceKey = "resource/" + Resource.Nebulite;
    private static final int MATERIALS = 1000;
    ArrayList<WeaponType> defaultWeapons = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        upgradeTree = new UpgradeTree();
        gameStateObserver = mock(GameStateObserver.class);
        mock(ServiceLocator.class);
        ServiceLocator.registerGameStateObserverService(gameStateObserver);
        defaultWeapons.add(WeaponType.WOODHAMMER);
        defaultWeapons.add(WeaponType.RANGED_SLINGSHOT);
        defaultWeapons.add(WeaponType.MELEE_WRENCH);
    }

    @Test
    public void testGetUnlockedWeaponsSize() {
        assertEquals(3, upgradeTree.getUnlockedWeapons().size());
    }

    @Test
    public void testIsUnlocked() {
        // Ensure the defaults are true and nonUnlocks are false
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.WOODHAMMER));
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STEELHAMMER));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.MELEE_WRENCH));
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_BOOMERANG));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_SLINGSHOT));
    }

    @Test
    public void testDefaultWeapons() {
        // ensure the tree contains only the default weapons
        assertEquals(defaultWeapons, upgradeTree.getUnlockedWeapons());
    }

    @Test
    public void testUnlockWeapon() {
        // Check a weapon is locked, unlock it, then check its unlocked
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER));
        upgradeTree.unlockWeapon(WeaponType.STONEHAMMER);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER));
    }

    @Test void testUnlockMultipleWeapons() {
        // Check stonehammer unlocks correctly
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER));
        upgradeTree.unlockWeapon(WeaponType.STONEHAMMER);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER));

        // Check steelhammer unlocks correctly
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STEELHAMMER));
        upgradeTree.unlockWeapon(WeaponType.STEELHAMMER);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STEELHAMMER));

        // Check ranged wrench unlocks correctly
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_BOOMERANG));
        upgradeTree.unlockWeapon(WeaponType.RANGED_BOOMERANG);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_BOOMERANG));

        // Ensure the unlocked weapons is the same as the current unlocked weapons
        ArrayList<WeaponType> dummy = new ArrayList<WeaponType>(
                Arrays.asList(
                        WeaponType.WOODHAMMER, WeaponType.RANGED_SLINGSHOT, WeaponType.MELEE_WRENCH,
                        WeaponType.STONEHAMMER, WeaponType.STEELHAMMER, WeaponType.RANGED_BOOMERANG));
        assertEquals(dummy, upgradeTree.getUnlockedWeapons());
    }

    @Test
    public void testGetStartingMaterials() {
        // Ensure it returns the starting materials based on game state observer
        when(gameStateObserver.getStateData(resourceKey)).thenReturn(MATERIALS);
        assertEquals(MATERIALS, (int) gameStateObserver.getStateData(resourceKey));
    }

    @Test
    public void testSubtractMaterials() {
        int subtractionAmount = 50;

        // Setup mock
        when(gameStateObserver.getStateData(resourceKey)).thenReturn(MATERIALS);

        upgradeTree.subtractMaterials(subtractionAmount);

        // Update mock
        when(gameStateObserver.getStateData(resourceKey)).thenReturn(MATERIALS - subtractionAmount);

        assertEquals(upgradeTree.getMaterials(), MATERIALS - subtractionAmount);
    }
}
