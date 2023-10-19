package com.csse3200.game.ai.tasks;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Task-based AI component. Given a list of tasks with priorities, the AI component will run the
 * highest priority task each frame. Tasks can be made up of smaller sub-tasks. A negative priority
 * indicates that the task should not be run.
 *
 * <p>This is a simple implementation of Goal-Oriented Action Planning (GOAP), a common AI decision
 * algorithm in games that's more powerful than Finite State Machines (FSMs) (State pattern).
 */
public class AITaskComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

  private final List<PriorityTask> priorityTasks = new ArrayList<>();
  private PriorityTask currentTask;

  /**
   * Add a priority task to the list of tasks. This task will be run only when it has the highest
   * priority, and can be stopped to run a higher priority task.
   *
   * @param task Task to add
   * @return self
   */
  public AITaskComponent addTask(PriorityTask task) {
    logger.debug("{} Adding task {}", this, task);
    priorityTasks.add(task);
    task.create(this);

    return this;
  }

  /**
   * Evaluates and runs the highest priority task each frame. If a new task with a higher priority
   * becomes available, the current task will be stopped, and the new task will begin.
   * Tasks with negative priorities are not executed.
   */
  @Override
  public void update() {
    PriorityTask desiredtask = getHighestPriorityTask();
    if (desiredtask == null || desiredtask.getPriority() < 0) {
      return;
    }

    if (desiredtask != currentTask) {
      changeTask(desiredtask);
    }
    currentTask.update();
  }

  /**
   * Cleanup method which ensures that the current task (if any) is properly terminated when the
   * component is disposed of.
   */
  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
    priorityTasks.clear();
  }

  /**
   * Retrieves the task with the highest priority from the list of tasks.
   *
   * @return The highest priority task, or null if there are no tasks or all have negative priorities.
   */
  private PriorityTask getHighestPriorityTask() {
    try {
      return Collections.max(priorityTasks, Comparator.comparingInt(PriorityTask::getPriority));
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  /**
   * Stops the current task and starts the specified new task as the current task.
   *
   * @param desiredTask The task to be started as the new current task.
   */
  private void changeTask(PriorityTask desiredTask) {
    logger.debug("{} Changing to task {}", this, desiredTask);
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = desiredTask;
    if (desiredTask != null) {
      desiredTask.start();
    }
  }
}
