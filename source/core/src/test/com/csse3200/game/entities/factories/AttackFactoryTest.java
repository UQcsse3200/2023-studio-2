package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.Weapons.SpecWeapon.BoomerangController;
import com.csse3200.game.components.Weapons.SpecWeapon.NukeController;
import com.csse3200.game.components.Weapons.SpecWeapon.Projectile.HomingMissileSprayProjectileController;
import com.csse3200.game.components.Weapons.SpecWeapon.Swing.MeleeSwingController;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.csse3200.game.entities.factories.AttackFactory.createAttack;
import static com.csse3200.game.entities.factories.AttackFactory.createAttacks;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class AttackFactoryTest {

    @Mock
    InventoryComponent inventoryComponent;
    @Mock
    Entity player;

    static final WeaponConfigs configs =
            FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");

    @BeforeEach
    void setUp() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        lenient().when(player.getPosition()).thenReturn(new Vector2(0,0));
        lenient().when(player.getCenterPosition()).thenReturn(new Vector2(0,0));
        lenient().when(player.getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);
    }

    @Test
    void wrenchControllerTest(){
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.MELEE_WRENCH);

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, 0);
        assertNotNull(attack.getKey());
        assertNotNull(attack.getKey().getComponent(MeleeSwingController.class));
    }

    @Test
    void katanaControllerTest(){
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.MELEE_KATANA);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, 0);
        assertNotNull(attack.getKey());
        assertNotNull(attack.getKey().getComponent(MeleeSwingController.class));
    }

    @Test
    void boomerangControllerTest(){
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.RANGED_BOOMERANG);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, 0);
        assertNotNull(attack.getKey());
        assertNotNull(attack.getKey().getComponent(BoomerangController.class));
    }

    @Test
    void missileControllerTest(){
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.RANGED_MISSILES);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, 1);
        assertNotNull(attack.getKey());
        assertNotNull(attack.getKey().getComponent(HomingMissileSprayProjectileController.class));
    }

    @Test
    void wrenchCombatComponentsTest(){
        int attackNumber = 0;
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.MELEE_KATANA);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, attackNumber);
        assertNotNull(attack.getValue());
        assertNotNull(attack.getKey().getComponent(PhysicsComponent.class));
        assertNotNull(attack.getKey().getComponent(HitboxComponent.class));
        assertNotNull(attack.getKey().getComponent(TouchAttackComponent.class));
        assertNotNull(attack.getKey().getComponent(CombatStatsComponent.class));
    }

    @Test
    void sniperAudioVisualComponentsTest(){
        int attackNumber = 0;
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.RANGED_SNIPER);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, attackNumber);
        assertNotNull(attack.getValue());
        assertNotNull(attack.getKey().getComponent(AnimationRenderComponent.class));
        assertNotNull(attack.getKey().getComponent(SoundComponent.class));
    }

    @Test
    void wrenchDamageTest(){
        int attackNumber = 0;
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.MELEE_KATANA);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, attackNumber);
        var attackDamage = attack.getKey().getComponent(CombatStatsComponent.class).getAttack();
        assertEquals(attackDamage, config.damage);
    }

    @Test
    void nukeControllerTest(){
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.RANGED_NUKE);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, 0);
        assertNotNull(attack.getKey());
        assertNotNull(attack.getKey().getComponent(NukeController.class));
    }

    @Test
    void katanaFirstSpawnDelayTest(){
        int attackNumber = 0;
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.MELEE_KATANA);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, attackNumber);
        assertNotNull(attack.getValue());
        assertEquals(attack.getValue(), 0);
    }

    @Test
    void spawnDelayTest(){
        int attackNumber = 1;
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.RANGED_MISSILES);
        Entity player = new Entity();

        Pair<Entity, Integer> attack = createAttack(config, new Vector2(0, 0), player, attackNumber);
        assertNotNull(attack.getValue());
        assertEquals(attack.getValue(), 400);
    }

    @Test
    void beeMultipleProjectilesTest(){
        WeaponConfig config = configs.GetWeaponConfig(WeaponType.RANGED_SNIPER);
        when(player.getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);
        when(inventoryComponent.getCurrentAmmo()).thenReturn(30);

        ArrayList<Pair<Entity, Integer>> attacks = createAttacks(WeaponType.RANGED_SNIPER, new Vector2(0, 0), player);
        assertEquals(attacks.size(), config.projectiles);
    }

    @Test
    void notEnoughAmmoTest(){
        when(inventoryComponent.getCurrentAmmo()).thenReturn(0);

        ArrayList<Pair<Entity, Integer>> attacks = createAttacks(WeaponType.RANGED_SNIPER, new Vector2(0, 0), player);
        assertNull(attacks);
    }
}