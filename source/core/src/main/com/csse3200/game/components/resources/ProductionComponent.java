package com.csse3200.game.components.resources;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PopupFactory;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class ProductionComponent extends Component {

    // Timer used to track time since last tick
    GameTime timer;

    // The desired amount of time (miliseconds) between each tic
    long tickRate;

    // The amount of resource produced on each tick
    int tickSize;

    // The time of the last tick
    long lastTime;

    public Resource getProduces() {
        return produces;
    }

    // The resource type this produces
    Resource produces;

    // Does not effect production, that scales by raw health, this is downstream of that
    // used to notify state observer of extractor count changes
    boolean damaged;

    /**
     * ProductionComponent allows an entity to produce resources on some real time interval and send them to
     * the gameState and event handler.
     *
     * @param produces the resource type this should produce
     * @param tickRate the amount of miliseconds between ticks (not guaranteed but catchup is performed if a tick is missed)
     * @param tickSize the amount of the resource to produce on each tick
     */
    public ProductionComponent(Resource produces, long tickRate, int tickSize) {
        this.timer = new GameTime();
        this.produces = produces;
        this.tickRate = tickRate;
        this.tickSize = tickSize;
        this.lastTime = timer.getTime();
        this.damaged = true;
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

    @Override
    public void update() {
        // Grab useful services and components:
        GameStateObserver gameStateObserver = ServiceLocator.getGameStateObserverService();
        CombatStatsComponent combatStats = this.getEntity().getComponent(CombatStatsComponent.class);

        // If our health JUST crossed a threshold, we notify the observer
        if (combatStats != null && combatStats.getHealth() <= 0 && !this.damaged) {
            this.damaged = true;
            gameStateObserver.trigger("extractorsAdd", this.produces.toString(), -1);
        } else if (combatStats != null && combatStats.getHealth() > 0 && this.damaged) {
            gameStateObserver.trigger("extractorsAdd", this.produces.toString(), 1);
            this.damaged = false;
        }

        super.update();
        while (this.timer.getTime() - this.lastTime >= this.tickRate ) {
            this.getEntity().getEvents().trigger("produceResource", this.produces, this.tickSize);
            int produced = (int) ((long) this.tickSize * this.getProductionModifier());
            gameStateObserver.trigger("resourceAdd", this.produces.toString(), produced);
            this.lastTime += this.tickRate;

            if (!this.damaged) {
                Entity popup = new PopupFactory().createPopup(this.produces);
                ServiceLocator.getEntityPlacementService().placeEntityAt(popup, this.entity.getPosition().add(this.entity.getScale().scl(0.65F)));
            }
        }
    }

    public void setTimer(GameTime timer) {
        this.timer = timer;
    }
}
