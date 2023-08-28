package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.physics.components.HitboxComponent;

import java.util.Timer;
import java.util.TimerTask;

public class DeathComponent extends Component {
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;


    public DeathComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionEnd", this::kill);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void kill(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }
        if (combatStats.isDead()) {
            entity.getEvents().trigger("dispose");
            // Schedule a task to execute entity::dispose after a delay
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(entity::dispose);
                }
            }, 2000); // 10 milliseconds delay

        }
    }
}