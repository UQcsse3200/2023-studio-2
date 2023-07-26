package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class WaitTaskTest {
  @Test
  void shouldWaitUntilTime() {
    GameTime time = mock(GameTime.class);
    when(time.getTime()).thenReturn(1000L);
    ServiceLocator.registerTimeSource(time);

    WaitTask task = new WaitTask(5f);
    task.start();
    assertEquals(Status.ACTIVE, task.getStatus());

    when(time.getTime()).thenReturn(5000L);
    task.update();
    assertEquals(Status.ACTIVE, task.getStatus());

    when(time.getTime()).thenReturn(6100L);
    task.update();
    assertEquals(Status.FINISHED, task.getStatus());
  }
}