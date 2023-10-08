package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.enemy.boss.God;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GodTest {
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time, game area
        RenderService renderService = new RenderService();

        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }
    @Test
    void shouldMove(){
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);
        ChaseTask chaseTask = new ChaseTask(target, 10, 5, 10, 2f);
        chaseTask.create(() -> entity);
        Assertions.assertEquals(chaseTask.getPriority(), 10);

    }
    private Entity makePhysicsEntity() {
        return new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent());
    }
}
