package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.csse3200.game.entities.factories.EnemyFactory.createEnemy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class EnemyFactoryTest {
    @Mock
    ShapeRenderer shapeRenderer;
    @Mock
    Box2DDebugRenderer physicsRenderer;
    DebugRenderer debugRenderer;

    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/enemy.json");

    @BeforeEach
    void setUp() {
        debugRenderer = new DebugRenderer(physicsRenderer, shapeRenderer);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    // TODO: Remove - Legacy tests

    /**
     * Test method for the getEnemyscale method in the EnemyFactory class.
     * It verifies that the scale values returned by the getEnemyscale method
     * Valid cases (expected scales):
     * - For Ranged enemy type, the scale should be 2.0f.
     * - For Melee enemy type, the scale should be 1.8f.
     * - For BossRanged enemy type, the scale should be 2.2f.
     * - For BossMelee enemy type, the scale should be 2.4f.
     */
//    @Test
//    void getEnemyScaleTest() {
//
//        assertEquals(2.0f, EnemyFactory.getEnemyscale(configs.GetEnemyConfig(EnemyType.Melee,EnemyBehaviour.PTE)));
//        assertEquals(1.8f, EnemyFactory.getEnemyscale(configs.GetEnemyConfig(EnemyType.Ranged,EnemyBehaviour.PTE)));
//        assertEquals(2.2f, EnemyFactory.getEnemyscale(configs.GetEnemyConfig(EnemyType.Melee,EnemyBehaviour.PTE)));
//        assertEquals(4.4f, EnemyFactory.getEnemyscale(configs.GetEnemyConfig(EnemyType.Melee,EnemyBehaviour.PTE)));
//    }

    /**
     * Melee PTE Enemy distinguished by stat traits
     * "meleeEnemyPTE":
     *  - "health": 20,
     *  - "baseAttack": 10,
     *  - "speed": 5,
     *  - "behaviour": "PTE",
     *  - "atlas": "images/enemy/rangeEnemy.atlas"
     *  Passes Test if created enemy contains the correct health and base attack
     */
    @Test
    void createEnemyPTETest(){
        ArrayList<Entity> targetList = new ArrayList<>();
        Entity newPlayer = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        targetList.add(newPlayer);
        // List should contain one empty Entity
        Entity enemy = createEnemy(EnemyType.Melee, EnemyBehaviour.PTE);

        // Checking Health matches PTE Melee Enemy
        assertEquals(enemy.getComponent(CombatStatsComponent.class).getHealth(), 20);
        // Checking Base Attack matches PTE Melee Enemy
        assertEquals(enemy.getComponent(CombatStatsComponent.class).getBaseAttack(), 10);
    }

    /**
     * Melee DTE Enemy distinguished by stat traits
     * "meleeEnemyDTE":
     *     "health": 50,
     *     "baseAttack": 5,
     *     "speed": 5,
     *     "behaviour": "DTE",
     *     "atlas": "images/enemy/troll_enemy.atlas"
     * Passes test if correct stats are returned
     */
    @Test
    void createEnemyDTETest() {
        ArrayList<Entity> targetList = new ArrayList<>();
        Entity structure = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE));
        targetList.add(structure);
        // List should contain one empty Entity
        Entity enemy = createEnemy(EnemyType.Melee, EnemyBehaviour.DTE);
        // Checking Health matches PTE Melee Enemy
        assertEquals(enemy.getComponent(CombatStatsComponent.class).getHealth(), 50);
        // Checking Base Attack matches PTE Melee Enemy
        assertEquals(enemy.getComponent(CombatStatsComponent.class).getBaseAttack(), 5);
    }

    /**
     * Ranged PTE Enemy Test
     * "rangeEnemyPTE":
     *     "health": 40,
     *     "baseAttack": 10,
     *     "speed": 5,
     *     "behaviour": "PTE",
     *     "atlas": "images/base_enemy.atlas"
     * Passes if successfully creates Entity with proposed traits
     */
    @Test
    void createEnemyRangePTETest() {
        ArrayList<Entity> targetList = new ArrayList<>();
        Entity structure = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE));
        targetList.add(structure);
        // List should contain one empty Entity
        Entity enemy = createEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);
        // Checking Health matches PTE Melee Enemy
        assertEquals(enemy.getComponent(CombatStatsComponent.class).getHealth(), 40);
        // Checking Base Attack matches PTE Melee Enemy
        assertEquals(enemy.getComponent(CombatStatsComponent.class).getBaseAttack(), 10);
    }

    /**
     * Creates new boss with the following stats:
     * "meleeBossPTE":
     *     "health": 100,
     *     "baseAttack": 25,
     *     "speed": 8,
     *     "specialAttack": 40,
     *     "atlas": "images/boss_enemy.atlas"
     * Passes if stats created in new entity equal the proposed health and base attack
     */
    @Test
    void createBossEnemyPTETest() {
        ArrayList<Entity> targetList = new ArrayList<>();
        Entity structure = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        targetList.add(structure);
        // List should contain one empty Entity
        Entity boss = createEnemy(EnemyType.BossMelee, EnemyBehaviour.PTE);
        // Checking Health matches PTE Melee Enemy
        assertEquals(boss.getComponent(CombatStatsComponent.class).getHealth(), 100);
        // Checking Base Attack matches PTE Melee Enemy
        assertEquals(boss.getComponent(CombatStatsComponent.class).getBaseAttack(), 25);
    }

    @Test
    void createEnemyMeleePTEConfigTest(){
        EnemyConfig config = configs.GetEnemyConfig(EnemyType.Melee, EnemyBehaviour.PTE);
        Entity enemy = createEnemy(config);

        // Check Health & BaseAttack was set appropriately based on type and behaviour
        assertEquals(20, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
    }

    @Test
    void createEnemyMeleeDTEConfigTest() {
        EnemyConfig config = configs.GetEnemyConfig(EnemyType.Melee, EnemyBehaviour.DTE);
        Entity enemy = createEnemy(config);

        // Check Health & BaseAttack was set appropriately based on type and behaviour
        assertEquals(50, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(5, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
    }

    @Test
    void createEnemyRangedPTEConfigTest(){
        EnemyConfig config = configs.GetEnemyConfig(EnemyType.Ranged, EnemyBehaviour.PTE);
        Entity enemy = createEnemy(config);

        // Check Health & BaseAttack was set appropriately based on type and behaviour
        assertEquals(40, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
    }

    @Test
    void createBossEnemyMeleePTEConfigTest() {
        EnemyConfig config = configs.GetEnemyConfig(EnemyType.BossMelee, EnemyBehaviour.PTE);
        Entity enemy = createEnemy(config);

        // Check Health & BaseAttack was set appropriately based on type and behaviour
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(25, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
    }

    @Test
    void createBossEnemyRangedPTEConfigTest() {
        EnemyConfig config = configs.GetEnemyConfig(EnemyType.BossRanged, EnemyBehaviour.PTE);
        Entity enemy = createEnemy(config);

        // Check Health & BaseAttack was set appropriately based on type and behaviour
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(25, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
    }


}
