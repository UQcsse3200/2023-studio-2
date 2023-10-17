package com.csse3200.game.components.enemy;


import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RingBurstComponentTest {
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
    void shouldRingBurst() {
        Entity target = new Entity();
        target.create();
        Entity entity = new Entity().addComponent(new PhysicsMovementComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(100, 10, 1,false));
        Component attackComponent = new RingBurstComponent(entity);
        entity.addComponent(attackComponent);
        entity.create();
        int halfHp = entity.getComponent(CombatStatsComponent.class).getMaxHealth() / 2;
        entity.getComponent(CombatStatsComponent.class).setHealth(halfHp);
        assertTrue(entity.getComponent(RingBurstComponent.class).hasShot());
    }
}
