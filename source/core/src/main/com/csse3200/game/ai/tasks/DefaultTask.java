package com.csse3200.game.ai.tasks;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 */
public abstract class DefaultTask implements Task {
  protected TaskRunner owner;
  protected Status status = Status.INACTIVE;

  @Override
  public void create(TaskRunner taskRunner) {
    this.owner = taskRunner;
  }

  @Override
  public void start() {
    status = Status.ACTIVE;
  }

  @Override
  public void update() {}

  @Override
  public void stop() {
    status = Status.INACTIVE;
  }

  @Override
  public Status getStatus() {
    return status;
  }
}
