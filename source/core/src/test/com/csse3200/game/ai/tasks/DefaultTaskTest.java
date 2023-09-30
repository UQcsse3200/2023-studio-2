package com.csse3200.game.ai.tasks;


import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
class DefaultTaskTest {
  @Test
  void shouldHaveCorrectStatus() {
    DefaultTask task = spy(DefaultTask.class);
    Entity entity = new Entity();
    assertEquals(Status.INACTIVE, task.getStatus());

    task.start();
    assertEquals(Status.ACTIVE, task.getStatus());

    task.stop();
    assertEquals(Status.INACTIVE, task.getStatus());
  }
}