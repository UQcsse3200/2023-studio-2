package com.csse3200.game.components.enemy;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
public class SprayComponentTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerStructurePlacementService(new StructurePlacementService(new EventHandler()));
    }

    /**
     * Tests the special attack activates when 50% hp
     */
    @Test
    void shouldSprayAttack() {
        Entity target = new Entity();
        target.create();
        Entity entity = new Entity().addComponent(new PhysicsMovementComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(100, 10, 1,false));
        Component attackComponent = new SprayComponent(target, entity);
        entity.addComponent(attackComponent);
        entity.create();

        long startTime = System.currentTimeMillis();
        long duration = 6000; // 6 seconds in milliseconds

        while (System.currentTimeMillis() - startTime < duration) {
            entity.earlyUpdate();
        }
        assertTrue(entity.getComponent(SprayComponent.class).isReadyToShoot());
    }
}
