package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

public class SpawnerComponent extends Component {
    // Timer used to track time since last tick
    GameTime timer;

    // The desired amount of time (seconds) between each tick
    long tickRate;

    // The time of the last tick
    long lastTime;

    // The enemy this spawns
    ArrayList<Entity> targets;

    EnemyType type;

    EnemyBehaviour behaviour;

    int count;

    /**
     * SpawnerComponent allows an entity to spawn enemies on some real time interval and send them to
     * the gameState and event handler.
     *
     * @param targets the list of enemy targets
     * @param tickRate the amount of seconds between ticks (not guaranteed but catchup is performed if a tick is missed)
     * @param type the type of enemy e.g.(Melee, Ranged) (Recommend to use for just small enemies)
     * @param behaviour the behaviour of the enemy, what it will prioritise
     */
    public SpawnerComponent(ArrayList<Entity> targets, long tickRate, EnemyType type, EnemyBehaviour behaviour, int count) {
        this.timer = new GameTime();
        this.tickRate = tickRate;
        this.lastTime = timer.getTime();
        this.targets = targets;
        this.type = type;
        this.behaviour = behaviour;
        this.count = count;
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void update() {
        int spawnedAmount = 0;
        super.update();
        Vector2 worldPos;
        while (this.timer.getTimeSince(this.lastTime) >= this.tickRate) {
            if (spawnedAmount != count) {
                worldPos = entity.getCenterPosition();
                Entity enemy = EnemyFactory.createEnemy(targets, type, behaviour);
                ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(enemy, worldPos);
                spawnedAmount++;
                this.lastTime += this.tickRate;
            } else {
                break;
            }
        }
    }
}
