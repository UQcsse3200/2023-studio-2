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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

    private WeaponConfigs weaponConfigs;
    private PlayerConfig playerConfig;
    private InventoryComponent inventory;

    @BeforeEach
    void beforeEach() {
        weaponConfigs = FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
        playerConfig = FileLoader.readClass(PlayerConfig.class, "configs/player.json");
        inventory = new InventoryComponent(weaponConfigs, playerConfig);
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
    public void testEquippedDefault() {
        assertEquals("melee", inventory.getEquipped());
    }

    @Test
    void testSetEquiped() {

        inventory = new InventoryComponent(weaponConfigs, playerConfig);
        inventory.setEquipped("melee");
        assertEquals("melee", inventory.getEquipped());
    }

    public void testReplaceSlotWithWeapon() {
        inventory.replaceSlotWithWeapon("ranged", WeaponType.RANGED_BOOMERANG);
        assertEquals(WeaponType.RANGED_BOOMERANG, inventory.getEquippedType());
    }

    @Test
    void testGetEquippedType() {
        inventory.setEquipped("building");
        assertEquals(WeaponType.WOODHAMMER, inventory.getEquippedType());
    }

    @Test
    void testChangeEquippedType() {
        inventory.setEquipped("building");
        inventory.changeEquipped(WeaponType.MELEE_WRENCH);
        assertEquals(WeaponType.MELEE_WRENCH, inventory.getEquippedType());
    }

    @Test
    public void testChangeEquippedAmmo() {
        inventory.changeEquippedAmmo(10);
        assertEquals(30, inventory.getCurrentAmmo());
    }

    @Test
    public void testGetEquippedCooldown() {
        int initialCooldown = inventory.getEquippedCooldown();
        inventory.setEquippedCooldown(5);
        assertEquals(5, inventory.getEquippedCooldown());
        inventory.setEquippedCooldown(initialCooldown);
    }
}