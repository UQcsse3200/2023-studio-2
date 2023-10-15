package com.csse3200.game.components.player;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.components.upgradetree.UpgradeTree;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class InventoryDisplayComponentTest {

    @Mock
    EntityService entityService;
    @Mock
    Entity player;
    @Mock
    InventoryComponent inventoryComponent;
    @Mock
    EventHandler eventHandler;
    @Mock
    UpgradeTree upgradeTree;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerEntityService(entityService);
        when(entityService.getPlayer()).thenReturn(player);
        when(player.getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);
        when(player.getEvents()).thenReturn(eventHandler);
        lenient().when(player.getComponent(UpgradeTree.class)).thenReturn(upgradeTree);
    }

    @Test
    void testMakeTable() {
        var component = new InventoryDisplayComponent();
        component.makeTable();
        assertNotNull(component.table);
    }

    @Test
    void testMakeHotbar() {
        var component = new InventoryDisplayComponent();
        when(upgradeTree.getUnlockedWeaponsConfigs()).thenReturn(new ArrayList<>());
        component.makeHotbar();
    }


    @AfterEach
    void afterEach() {
        ServiceLocator.clear();
    }

    @Test
    void testConstructor() {
        new InventoryDisplayComponent();
        verify(player).getEvents();
        verify(player).getComponent(InventoryComponent.class);
        assertNotNull(player);
    }

    @Test
    void testEquipEvent() {
        var component = new InventoryDisplayComponent();
        component.equipEvent();

        // Ensures the equip trigger is called twice
        verify(inventoryComponent, times(2)).getEquippedWeapons();
    }

    @Test
    void testUpdateButtonTableColor() {
        var component = new InventoryDisplayComponent();

        Button button = new Button();
        WeaponType equippedWeapon = mock(WeaponType.class);
        WeaponType nonEquipped = mock(WeaponType.class);
        when(inventoryComponent.getEquippedType()).thenReturn(equippedWeapon);

        // This ensures non-equipped weapons are greyed out (all rgba is 0.5f)
        component.updateButtonColor(button, nonEquipped);
        assertEquals(0.5f, button.getColor().r);
        assertEquals(0.5f, button.getColor().g);
        assertEquals(0.5f, button.getColor().b);
        assertEquals(0.5f, button.getColor().a);

        // This ensures equipped weapons are NOT greyed out
        component.updateButtonColor(button, equippedWeapon);
        assertEquals(1f, button.getColor().r);
        assertEquals(1f, button.getColor().g);
        assertEquals(1f, button.getColor().b);
        assertEquals(1f, button.getColor().a);
    }
}