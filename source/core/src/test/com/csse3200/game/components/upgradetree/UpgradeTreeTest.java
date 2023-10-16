package com.csse3200.game.components.upgradetree;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.WOODHAMMER.toString()));
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STEELHAMMER.toString()));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.MELEE_WRENCH.toString()));
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_HOMING.toString()));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_SLINGSHOT.toString()));
    }

    @Test
    public void testDefaultWeapons() {
        // ensure the tree contains only the default weapons
        assertEquals(3, upgradeTree.getUnlockedWeapons().size());
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.MELEE_WRENCH.toString()));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.WOODHAMMER.toString()));
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_SLINGSHOT.toString()));
    }

    /**
     * the below Test has been implemented to
     * test the Ui of unlocking weapon and
     * also to test the sound of it
     */
    @Test
    public void testUnlockWeapon() {

        Gdx.app = mock(Application.class);
        // Check a weapon is locked, unlock it, then check its unlocked
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER.toString()));
        upgradeTree.unlockWeapon(WeaponType.STONEHAMMER.toString());
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.STONEHAMMER.toString()));

//        instance for UpgradeTree
        UpgradeTree sound = new UpgradeTree();

//        Setting the 'unlockWeaponSound' as the entity for sound events.
        Entity unlockWeaponEvent = mock(Entity.class);

//        Setting the 'unlockWeaponSound' as the entity for sound events.
        sound.setEntity(unlockWeaponEvent);

//        Mocking the event handling for the 'unlockWeaponSound' entity.
        when(unlockWeaponEvent.getEvents()).thenReturn(mock(EventHandler.class));

        Object weapon = new Object();
//        Testing the whole trigger unlockWeaponEvent to test Player's dodge Sound
        sound.unlockWeapon(weapon);
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
        assertFalse(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_HOMING));
        upgradeTree.unlockWeapon(WeaponType.RANGED_HOMING);
        assertTrue(upgradeTree.isWeaponUnlocked(WeaponType.RANGED_HOMING));

        // Ensure there are now 6 weapons in total unlocked, including default weapons
        assertEquals(6, upgradeTree.getUnlockedWeapons().size());
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