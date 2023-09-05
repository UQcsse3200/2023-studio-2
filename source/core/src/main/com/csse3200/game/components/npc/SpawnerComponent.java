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

    int spawnedAmount = 0;

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
        super.update();
        Vector2 worldPos;
        while (this.timer.getTimeSince(this.lastTime) >= this.tickRate && spawnedAmount < count) {
                worldPos = entity.getCenterPosition();
                Entity enemy = EnemyFactory.createEnemy(targets, type, behaviour);
                ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(enemy, worldPos);
                spawnedAmount += 1;
                System.out.println(spawnedAmount);
                this.lastTime += this.tickRate;
        }
    }

    /*        long currentTime = timer.getTime();

        // to know when to start new wave
        if (spawnedAmount < count && currentTime - lastTime >= tickRate) {
            // Spawn enemies for the current wave
            switch (currentWave) {
                case 0:
                    spawnMeleeEnemies(10);
                    break;
                case 1:
                    spawnMeleeEnemies(10);
                    spawnRangedEnemies(5);
                    break;
                case 2:
                    spawnMeleeEnemies(10);
                    spawnRangedEnemies(10);
                    spawnBoss();
                    break;
            }

            //  last wave start time upDate and increasing the wave counter
            lastTime = currentTime;
            currentWave++;

            //  wave delay
            if (currentWave < 3) {
                lastTime += spawnDelay; // Convert seconds to milliseconds
            }
        }
    }

    private void spawnMeleeEnemies(int count) {
        for (int i = 0; i < count && spawnedAmount < this.count; i++) {
            spawnEnemy(EnemyType.Melee);
        }
    }

    private void spawnRangedEnemies(int count) {
        for (int i = 0; i < count && spawnedAmount < this.count; i++) {
            spawnEnemy(EnemyType.Ranged);
        }
    }

    private void spawnBoss() {
        switch (currentWave) {
            case 2:
                spawnEnemy(EnemyType.BossMelee);
                break;
            case 3:
                spawnEnemy(EnemyType.BossRanged);
                break;
        }
    }

    private void spawnEnemy(EnemyType enemyType) {
        Vector2 worldPos = entity.getCenterPosition();
        Entity enemy = EnemyFactory.createEnemy(targets, enemyType, behaviour);
        ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(enemy, worldPos);
        spawnedAmount++;
    }
}*/
}
