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

    private int currentWave = 0;

    private long spawnDelay = 0;

    long waveDelay = 10000;
    /**
     * SpawnerComponent allows an entity to spawn enemies on some real time interval and send them to
     * the gameState and event handler.
     *
     * @param targets the list of enemy targets
     */
    public SpawnerComponent(ArrayList<Entity> targets) {
        this.timer = new GameTime();
        this.lastTime = timer.getTime();
        this.targets = targets;
    }

    @Override
    public void create() {
        super.create();
    }

    /**
     * Spawns a specified number of melee enemies.
     *
     * @param count The number of melee enemies to spawn.
     */
    private void spawnMeleeEnemies(int count) {
        for (int i = 0; i < count; i++) {
            spawnEnemy(EnemyType.Melee, EnemyBehaviour.PTE);
        }
    }

    /**
     * Spawns a specified number of ranged enemies.
     *
     * @param count The number of ranged enemies to spawn.
     */
    private void spawnRangedEnemies(int count) {
        for (int i = 0; i < count; i++) {
            spawnEnemy(EnemyType.Melee, EnemyBehaviour.DTE);
        }
    }

//    private void spawnBoss() {
//        switch (currentWave) {
//            case 2:
//                spawnEnemy(EnemyType.BossMelee);
//                break;
//            case 3:
//                spawnEnemy(EnemyType.BossRanged);
//                break;
//        }
//    }
    
    /**
     * Spawns an enemy with the specified type and behavior.
     *
     * @param enemyType  The type of enemy to spawn.
     * @param behaviour  The behavior of the enemy.
     */

    private void spawnEnemy(EnemyType enemyType, EnemyBehaviour behaviour) {
        Vector2 worldPos = entity.getCenterPosition();
        Entity enemy = EnemyFactory.createEnemy(targets, enemyType, behaviour);
        ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(enemy, worldPos);
    }

    @Override
    public void update() {
        super.update();
        if (this.timer.getTimeSince(this.lastTime) >= waveDelay) {
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
                    break;
            }
            
            currentWave++;
            this.lastTime += waveDelay;
            

          
        }
    }
}
