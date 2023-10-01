package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SpawnerConfig;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class SpawnerComponent extends Component {
    public static final long WAVE_DELAY = 3000;  // 20 seconds
    public static final long SPAWN_DELAY = 3000;  // 3 seconds

    public final GameTime timer;
    public final SpawnerConfig config;

    public long lastTime;
    public int currentWave = 0;
    public boolean isSpawning = false;
    public int enemiesToSpawn = 0;
    public int enemiesSpawned = 0;
    public int meleeEnemiesToSpawn = 0;
    public int rangedEnemiesToSpawn = 0;

    public SpawnerComponent(SpawnerConfig config) {
        this.timer = new GameTime();
        this.lastTime = timer.getTime();
        this.config = config;
    }

    // ... [Getter and Setter methods remain unchanged] ...

    @Override
    public void update() {
        long currentTime = timer.getTime();
        if (shouldSpawnNewWave(currentTime)) {
            handleNewWave(currentTime);
        }
        if (shouldSpawnEnemy(currentTime)) {
            handleEnemySpawn(currentTime);
        }
        if (shouldStopSpawning()) {
            resetSpawningState();
        }
    }

    public boolean shouldSpawnNewWave(long currentTime) {
        return !isSpawning && currentTime - lastTime >= WAVE_DELAY;
    }

    public boolean shouldSpawnEnemy(long currentTime) {
        return isSpawning && enemiesSpawned < enemiesToSpawn && currentTime - lastTime >= SPAWN_DELAY;
    }

    public boolean shouldStopSpawning() {
        return enemiesSpawned >= enemiesToSpawn;
    }

    public void handleNewWave(long currentTime) {
        int[] currentConfig;
        switch (currentWave) {
            case 0:
                currentConfig = config.wave1;
                break;
            case 1:
                currentConfig = config.wave2;
                break;
            case 2:
                currentConfig = config.wave3;
                break;
            default:
                return;
        }
        spawnEnemies(currentConfig[0], currentConfig[1]);
        currentWave++;
        lastTime = currentTime;
    }

    /**
     *
     * @param currentTime - checks the time to handle and release the new enemy wave
     *  The handleEnemySpawn() handles when to spawn enemies and
     *                    also trigger sound while spawning.
     */
    public void handleEnemySpawn(long currentTime) {
        if (meleeEnemiesToSpawn > 0) {
            spawnEnemy(EnemyType.Melee, EnemyBehaviour.PTE);
            meleeEnemiesToSpawn--;
            if (entity != null) {
                entity.getEvents().trigger("playSound", "enemySpawn"); // triggering spawning sound effects
            }
        } else if (rangedEnemiesToSpawn > 0) {
            spawnEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);
            rangedEnemiesToSpawn--;
            if (entity != null) {
                entity.getEvents().trigger("playSound", "enemySpawn"); // triggering spawning sound effects
            }
        }
        enemiesSpawned++;
        lastTime = currentTime;
    }

    public void resetSpawningState() {
        isSpawning = false;
        enemiesToSpawn = 0;
        enemiesSpawned = 0;
    }

    public void spawnEnemies(int meleeCount, int rangedCount) {
        isSpawning = true;
        enemiesToSpawn = meleeCount + rangedCount;
        enemiesSpawned = 0;
        meleeEnemiesToSpawn = meleeCount;
        rangedEnemiesToSpawn = rangedCount;
    }

    public void spawnEnemy(EnemyType enemyType, EnemyBehaviour behaviour) {
        Vector2 worldPos = entity.getCenterPosition();
        Entity enemy = EnemyFactory.createEnemy(enemyType, behaviour);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemy, worldPos);
    }
}

