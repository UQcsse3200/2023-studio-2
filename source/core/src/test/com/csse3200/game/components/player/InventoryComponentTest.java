package com.csse3200.game.components.player;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.structures.ToolsConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {
    private WeaponConfigs weaponConfigs;

    @BeforeEach
    void beforeEach() {
        weaponConfigs = FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
    }

    @Test
    void testConfig() {
        assertNotNull(weaponConfigs);
    }

    @Test
    void testGetEquipped() {
        InventoryComponent inventory = new InventoryComponent(weaponConfigs);

        inventory.setEquipped("ranged");
        assertEquals("ranged", inventory.getEquipped());

        inventory.setEquipped("melee");
        assertEquals("melee", inventory.getEquipped());

        inventory.setEquipped("building");
        assertEquals("building", inventory.getEquipped());
    }

    @Test
    void testSetEquiped() {
        InventoryComponent inventory = new InventoryComponent(weaponConfigs);
        inventory.setEquipped("melee");
        assertEquals("melee", inventory.getEquipped());
    }

    @Test
    void testGetEquippedType() {
    InventoryComponent inventory = new InventoryComponent(weaponConfigs);
    inventory.setEquipped("building");
    assertEquals(WeaponType.WOODHAMMER, inventory.getEquippedType());
    }

    @Test
    void testChangeEquippedType() {
    InventoryComponent inventory = new InventoryComponent(weaponConfigs);
    inventory.setEquipped("building");
    inventory.changeEquipped(WeaponType.MELEE_WRENCH);
    assertEquals(WeaponType.MELEE_WRENCH, inventory.getEquippedType());
    }
}