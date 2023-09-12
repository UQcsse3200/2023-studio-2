package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task that does nothing other than waiting for a given time. Status is Finished
 * after the time has passed.
 */
public class WaitTask extends DefaultTask {
  private final GameTime timeSource;
  private final float duration;
  private long endTime;

  /**
   * @param duration How long to wait for, in seconds.
   */
  public WaitTask(float duration) {
    timeSource = ServiceLocator.getTimeSource();
    this.duration = duration;
  }

  /**
   * Start waiting from now until duration has passed.
   */
  @Override
  public void start() {
    super.start();
    endTime = timeSource.getTime() + (int)(duration * 1000);
  }

  @Override
  public void update() {
    if (timeSource.getTime() >= endTime) {
      status = Status.FINISHED;
    }
  }
}
