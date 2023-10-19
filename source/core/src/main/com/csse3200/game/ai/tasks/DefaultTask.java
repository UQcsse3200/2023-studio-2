package com.csse3200.game.ai.tasks;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 */
public abstract class DefaultTask implements Task {
  /** The entity or system that owns and manages this task. */
  protected TaskRunner owner;

  /** The current status of the task, initially set to INACTIVE. */
  protected Status status = Status.INACTIVE;

  /**
   * Initializes the task with its owning entity or system.
   *
   * @param taskRunner The entity or system that will run and manage this task.
   */
  @Override
  public void create(TaskRunner taskRunner) {
    this.owner = taskRunner;
  }

  /**
   * Starts the task by setting its status to ACTIVE.
   */
  @Override
  public void start() {
    status = Status.ACTIVE;
  }

  /**
   * A placeholder for the update logic of the task. Derived classes can
   * override this method to provide specific task update behaviors.
   */
  @Override
  public void update() {}

  /**
   * Stops the task by setting its status to INACTIVE.
   */
  @Override
  public void stop() {
    status = Status.INACTIVE;
  }


  /**
   * Retrieves the current status of the task.
   */
  @Override
  public Status getStatus() {
    return status;
  }
}
