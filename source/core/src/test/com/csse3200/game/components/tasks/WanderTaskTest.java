package com.csse3200.game.components.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WanderTaskTest {
  @Mock
  GameTime gameTime;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }

  @Test
  void shouldTriggerEvent() {
    WanderTask wanderTask = new WanderTask(Vector2Utils.ONE, 1f);

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(wanderTask);
    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
    entity.create();

    // Register callbacks
    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderStart", callback);

    wanderTask.start();

    verify(callback).handle();
  }
}