package com.csse3200.game.ai.tasks;

/**
 * An AI task can be started and stopped at any time. When updating, the task can succeed or fail.
 */
public interface Task {

  /**
   * Create the task and attach it to the task runner.
   *
   * @param taskRunner Task runner to attach to
   */
  void create(TaskRunner taskRunner);

  /** Start running this task. This will usually be called by an AI controller. */
  void start();

  /** Run one frame of the task. Similar to the update() in Components. */
  void update();

  /** Stop the task immediately. This can be called at any time by the AI controller. */
  void stop();

  /**
   * Get the current status of the task.
   *
   * @return status
   */
  Status getStatus();

  enum Status {
    FINISHED, // The task has completed succesfully
    FAILED, // The task has failed
    ACTIVE, // The task is currently running
    INACTIVE // The task is currently not running
  }
}
