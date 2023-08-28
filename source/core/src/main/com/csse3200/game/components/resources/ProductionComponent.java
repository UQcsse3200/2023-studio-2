package com.csse3200.game.components.resources;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameState;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

public class ProductionComponent extends Component {
    GameTime timer;
    long tickRate;
    int tickSize;
    long lastTime;
    Resource produces;

    public ProductionComponent(Resource produces, long tickRate, int tickSize) {
        this.timer = new GameTime();
        this.produces = produces;
        this.tickRate = tickRate;
        this.tickSize = tickSize;
        this.lastTime = timer.getTime();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void update() {
        super.update();
        while (this.timer.getTimeSince(this.lastTime) >= this.tickRate ) {
            this.getEntity().getEvents().trigger("produceResource", this.produces, this.tickSize);
            ServiceLocator.getGameStateObserverService().trigger("resourceAdd", this.produces.toString(), this.tickSize);
            this.lastTime += this.tickRate;
        }
    }
}
