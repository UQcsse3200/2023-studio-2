package com.csse3200.game.components.upgradetree;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class UpgradeTreeTest {

    private UpgradeTree tree;
    private GameStateObserver gameStateObserver;
    private String resourceKey = "resource/" + Resource.Nebulite;
    private static final int MATERIALS = 1000;

    @BeforeEach
    public void setUp() {
        tree = new UpgradeTree();
        gameStateObserver = mock(GameStateObserver.class);
        mock(ServiceLocator.class);
        ServiceLocator.registerGameStateObserverService(gameStateObserver);
    }

    @Test
    public void testDefaults() {
        assertEquals(3, tree.getUnlockedWeaponsConfigs().size());
    }

    @Test
    public void testUnlock() {
        WeaponConfig weaponConfig = new WeaponConfig();

        tree.unlockWeapon(weaponConfig);

        assertTrue(tree.isWeaponUnlocked(weaponConfig));
    }

    @Test
    public void testIsUnlocked() {

        WeaponConfig notUnlocked = mock(WeaponConfig.class);

        assertFalse(tree.isWeaponUnlocked(notUnlocked));
    }

    @Test
    public void testUnlockTrigger() {
        Application app = mock(Application.class);
        Gdx.app = app;


        WeaponConfig dummy = mock(WeaponConfig.class);
        Entity unlockWeaponEvent = mock(Entity.class);
        when(unlockWeaponEvent.getEvents()).thenReturn(mock(EventHandler.class));
        tree.setEntity(unlockWeaponEvent);

        tree.unlockWeapon(dummy);
    }

    @Test
    public void testUnlockMultiple() {
        WeaponConfig weapon1 = mock(WeaponConfig.class);
        WeaponConfig weapon2 = mock(WeaponConfig.class);
        WeaponConfig weapon3 = mock(WeaponConfig.class);

        int initialCount = tree.getUnlockedWeaponsConfigs().size();

        tree.unlockWeapon(weapon1);
        tree.unlockWeapon(weapon2);
        tree.unlockWeapon(weapon3);

        assertEquals(initialCount + 3, tree.getUnlockedWeaponsConfigs().size());
    }

    @Test
    public void testGetMaterials() {
        when(gameStateObserver.getStateData(resourceKey)).thenReturn(MATERIALS);

        assertEquals(MATERIALS, tree.getMaterials());
    }

    @Test
    public void testSubtractMaterials() {
        int subtractionAmount = 50;
        when(gameStateObserver.getStateData(resourceKey)).thenReturn(MATERIALS);

        tree.subtractMaterials(subtractionAmount);

        when(gameStateObserver.getStateData(resourceKey)).thenReturn(MATERIALS - subtractionAmount);

        assertEquals(MATERIALS - subtractionAmount, tree.getMaterials());
    }
}