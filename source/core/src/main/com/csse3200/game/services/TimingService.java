package com.csse3200.game.services;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.services.ServiceLocator;

public class TimingService {
    private static final Logger logger = LoggerFactory.getLogger(TimingService.class);
    private static final int INITIAL_CAPACITY = 16;
    private static final String HOUR_UPDATE = "hourUpdate";
    private static final String DAY_UPDATE = "dayUpdate";
    private static final int MS_IN_HOUR = 30000; //30000;
    private int hour;
    private int day;
    private long timeBuffer;
    private long lastGameTime;
    private boolean paused;
    private final EventHandler events;


    public TimingService() {
        hour = 0;
        day = 0;
        paused = false;
        lastGameTime = ServiceLocator.getTimeSource().getTime();
        events = new EventHandler();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean state) {
        paused = state;
        if (state == true) {
            ServiceLocator.getTimeSource().setTimeScale(0);
        } else {
            ServiceLocator.getTimeSource().setTimeScale(1);
        }
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }
    public void setHour(int hour) {
        this.hour = hour;
        this.timeBuffer = 0;
        events.trigger("hourUpdate");
    }

    public void setDay(int day) {
        this.day = day;
        this.timeBuffer= 0;
        events.trigger("dayUpdate");
    }

    public EventHandler getEvents() {
        return events;
    }

    public void update() {
        // this time will be in ms
        long timePassed = ServiceLocator.getTimeSource().getTimeSince(lastGameTime);
        lastGameTime = ServiceLocator.getTimeSource().getTime();
        if (paused) {
            return;
        }
        timeBuffer += timePassed;
        // If time elapsed isn't one hour in the game, do nothing
        if (timeBuffer < MS_IN_HOUR) {
            return;
        }
        hour += 1;
        timeBuffer -= MS_IN_HOUR;

        // If hour is between 0 and 23, day hasn't elapsed, do nothing
        if (hour < 24) {
            events.trigger("hourUpdate");
            return;
        }
        hour -= 24;
        events.trigger("hourUpdate");
        day += 1;
        events.trigger("dayUpdate");

        // lose the game if the game reaches 30 days
//        if (day >= 30) {
//            ServiceLocator.getGameArea().getPlayer().getEvents().trigger("loseScreen");
//        }
    }

}