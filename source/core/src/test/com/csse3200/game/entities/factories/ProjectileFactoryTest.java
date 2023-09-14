package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.csse3200.game.entities.factories.EnemyFactory.createEnemy;
import static com.csse3200.game.entities.factories.ProjectileFactory.createEnemyBullet;

@ExtendWith(GameExtension.class)
public class ProjectileFactoryTest {
    @Mock
    ShapeRenderer shapeRenderer;
    @Mock
    Box2DDebugRenderer physicsRenderer;
    DebugRenderer debugRenderer;
    @BeforeEach
    void setUp() {
        debugRenderer = new DebugRenderer(physicsRenderer, shapeRenderer);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
    }
    /**
     * Melee PTE Enemy distinguished by stat traits
     * "meleeEnemyPTE":
     *  - "health": 20,
     *  - "baseAttack": 10,
     *  - "speed": 5,
     *  - "behaviour": "PTE",
     *  - "atlas": "images/rangeEnemy.atlas"
     *  Passes Test if created bullet contains the correct attack retrieved from the Melee PTE enemy
     */
    @Test
    void createEnemyBulletTest() {
        ArrayList<Entity> targetList = new ArrayList<>();
        Entity newPlayer = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        targetList.add(newPlayer);
        Entity enemy = createEnemy(targetList, EnemyType.Melee, EnemyBehaviour.PTE);
        Vector2 bulletPosition = new Vector2(1f, 1f);
        Entity bullet = createEnemyBullet(bulletPosition,enemy);

        // Checking Attack matches bullet damage
        Assertions.assertEquals(bullet.getComponent(CombatStatsComponent.class).getAttack(), 10);
    }
}
