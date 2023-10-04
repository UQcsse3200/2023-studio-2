package com.csse3200.game.components.explosives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

public class TimerAction extends Timer.Task {
    private final Runnable runnable;

    public TimerAction(Runnable runnable) {

        this.runnable = runnable;
    }

    @Override
    public void run() {
        Gdx.app.postRunnable(runnable);
    }
}
