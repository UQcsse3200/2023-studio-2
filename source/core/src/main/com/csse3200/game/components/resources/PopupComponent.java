package com.csse3200.game.components.resources;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.io.Serial;
import java.util.Timer;
import java.util.TimerTask;

public class PopupComponent extends Component {
    GameTime timer;
    double startTime;
    double lastTime;
    double speed;
    int duration;

    public PopupComponent(int duration, double speed) {
        this.timer = new GameTime();
        this.startTime = this.timer.getTime();
        this.lastTime = this.timer.getTime();
        this.speed = speed;
        this.duration = duration;
    }

    @Override
    public void update() {
        super.update();

        double since = this.timer.getTimeSince((long) lastTime);
        double lifespan = this.timer.getTimeSince((long) startTime);
        lastTime = timer.getTime();

        if (lifespan > duration) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(entity::dispose);
                }
            }, 2);
        }


        this.entity.setPosition(this.entity.getPosition().x, (float) (this.entity.getPosition().y + since * speed));
        this.entity.getComponent(TextureRenderComponent.class).setAlpha(1.0F - (float) (lifespan / duration));
    }
}
