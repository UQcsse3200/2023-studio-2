package com.csse3200.game.components.explosives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

/**
 * A Timer Task which takes in a runnable (parameterless method) and runs it as a postRunnable function when
 * the timer elapses.
 */
public class PostrunnableTask extends Timer.Task {
    private final Runnable runnable;

    /**
     * Creates the PostrunnableTask
     * @param runnable - the method to run.
     */
    public PostrunnableTask(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Overrides the run method to run the given runnable after the game finishes it's current frame calculations.
     */
    @Override
    public void run() {
        Gdx.app.postRunnable(runnable);
    }
}
