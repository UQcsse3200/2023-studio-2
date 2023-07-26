package com.csse3200.game.ai.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class AITaskComponentTest {
  @Test
  void shouldRunNothingWithNoTask() {
    AITaskComponent taskComponent = new AITaskComponent();
    taskComponent.update();
  }

  @Test
  void shouldNotRunNegativePriorityTask() {
    AITaskComponent taskComponent = new AITaskComponent();
    PriorityTask task = mock(PriorityTask.class);
    when(task.getPriority()).thenReturn(-1);
    taskComponent.addTask(task);
    taskComponent.update();
    verify(task, times(0)).start();
  }

  @Test
  void shouldRunSingleTask() {
    AITaskComponent taskComponent = new AITaskComponent();
    PriorityTask task = mock(PriorityTask.class);
    when(task.getPriority()).thenReturn(1);

    taskComponent.addTask(task);
    taskComponent.update();
    verify(task).start();
    verify(task).update();
  }

  @Test
  void shouldRunPriorityTask() {
    AITaskComponent taskComponent = new AITaskComponent();

    PriorityTask lowPriorityTask = mock(PriorityTask.class);
    when(lowPriorityTask.getPriority()).thenReturn(1);
    taskComponent.addTask(lowPriorityTask);

    PriorityTask highPriorityTask = mock(PriorityTask.class);
    when(highPriorityTask.getPriority()).thenReturn(2);
    taskComponent.addTask(highPriorityTask);
    taskComponent.update();

    verify(highPriorityTask).start();
    verify(highPriorityTask).update();
    verify(lowPriorityTask, times(0)).start();
    verify(lowPriorityTask, times(0)).update();

    // Swap priorities
    when(lowPriorityTask.getPriority()).thenReturn(3);
    taskComponent.update();

    verify(lowPriorityTask).start();
    verify(lowPriorityTask).update();
    verify(highPriorityTask).stop();
    verify(highPriorityTask, times(1)).update();
  }

  @Test
  void shouldStopTaskOnDispose() {
    AITaskComponent taskComponent = new AITaskComponent();
    PriorityTask task = mock(PriorityTask.class);
    when(task.getPriority()).thenReturn(1);
    taskComponent.addTask(task);

    taskComponent.update();
    taskComponent.dispose();

    verify(task).stop();
  }
}
