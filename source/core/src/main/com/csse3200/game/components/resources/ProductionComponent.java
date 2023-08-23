package com.csse3200.game.components.resources;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameState;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.security.Provider;
import java.util.HashMap;
import java.util.Map;


public class ProductionComponent extends Component {
    // Timer used to track time since last tick
    GameTime timer;

    // The desired amount of time (seconds) between each tick
    long tickRate;

    // The amount of resource produced on each tick
    int tickSize;

    // The time of the last tick
    long lastTime;

    // The resource type this produces
    Resource produces;

    /**
     * ProductionComponent allows an entity to produce resources on some real time interval and send them to
     * the gameState and event handler.
     *
     * @param produces the resource type this should produce
     * @param tickRate the amount of seconds between ticks (not guaranteed but catchup is performed if a tick is missed)
     * @param tickSize the amount of the resource to produce on each tick
     */
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

    /**
     * getProductionModifier returns a value between 0 and 1 for how efficient the extractor is.
     * For example a fully functioning extractor would have an efficiency of 1.0, and a broken one 0.0.
     *
     * @return long between 0 and 1.
     */
    public long getProductionModifier() {
        CombatStatsComponent combatStats = this.getEntity().getComponent(CombatStatsComponent.class);
        if (combatStats != null && combatStats.getHealth() <= 0) {
            return 0;
        }
        // All good, return the full amount
        return (long) 1.0;
    }

    /**
     * Produce resources and send them to GameState (and produceResource event).
     * Resources produced are based on how many ticks have occurred since the last update, and production quantity.
     */
    public void produce() {
        while (this.timer.getTimeSince(this.lastTime) >= this.tickRate) {
            this.getEntity().getEvents().trigger("produceResource", this.produces, this.tickSize);
            // Extract the current state from the ServiceLocator
            GameState state = ServiceLocator.getGameStateService();
            if (state == null) {
                return;
            }
            Map<String, Object> currentState = state.getStateData();
            HashMap<String, Object> newState = new HashMap<>(currentState);

            // Update the quantity of this resource
            int produced = (int) ((long) this.tickSize * this.getProductionModifier());
            int newAmount = (int) newState.getOrDefault("resource/" + this.produces.toString(), 0.0) + produced;
            newState.put("resource/" + this.produces.toString(), newAmount);

            // Send the updated value back
            state.setStateData(newState);
            this.lastTime += this.tickRate;
        }
    }

    @Override
    public void update() {
        super.update();
        this.produce();
    }
}
