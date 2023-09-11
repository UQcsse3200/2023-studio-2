package com.csse3200.game.components.resources;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameTime;

public class PopupComponent extends Component {
    GameTime timer;
    double startTime;
    double lastTime;
    int speed;
    int duration;

    public PopupComponent(int duration, int speed) {
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
        lastTime = timer.getTime();

        this.entity.setPosition(this.entity.getPosition().x, (float) (this.entity.getPosition().y + since * speed));
    }
}
