package com.csse3200.game.components.player;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.structures.ToolsConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

    @Mock
    private WeaponConfigs weaponConfigs;

    @Mock
    private PlayerConfig playerConfig;

    @Mock
    private InventoryComponent inventory;

    @BeforeEach
    void beforeEach() {
        weaponConfigs = mock(WeaponConfigs.class);
        playerConfig = mock(PlayerConfig.class);
        inventory = mock(InventoryComponent.class);

        LinkedHashMap<String, WeaponType> mockSlotTypeMap = new LinkedHashMap<>();
        mockSlotTypeMap.put("building", WeaponType.WOODHAMMER);
        when(inventory.getSlotTypeMap()).thenReturn(mockSlotTypeMap);
        when(inventory.getEquippedType()).thenReturn(WeaponType.WOODHAMMER);
    }

    @Test
    void testConfig() {
        assertNotNull(weaponConfigs);
    }

    @Test
    void testGetEquipped() {
        inventory = new InventoryComponent(weaponConfigs, playerConfig);
        inventory.setEquipped("ranged");
        assertEquals("ranged", inventory.getEquipped());

        inventory.setEquipped("melee");
        assertEquals("melee", inventory.getEquipped());

        inventory.setEquipped("building");
        assertEquals("building", inventory.getEquipped());
    }

    @Test
    void testSetEquiped() {
        inventory = new InventoryComponent(weaponConfigs, playerConfig);
        inventory.setEquipped("melee");
        assertEquals("melee", inventory.getEquipped());
    }

    @Test
    void testGetEquippedType() {
        inventory.setEquipped("building");
        assertEquals(WeaponType.WOODHAMMER, inventory.getEquippedType());
    }

    @Test
    void testChangeEquippedType() {
        when(inventory.getEquippedType()).thenReturn(WeaponType.MELEE_WRENCH);
        inventory.setEquipped("building");
        inventory.changeEquipped(WeaponType.MELEE_WRENCH);
        assertEquals(WeaponType.MELEE_WRENCH, inventory.getEquippedType());
    }
}