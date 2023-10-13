package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SpawnerConfig;
import com.csse3200.game.entities.enemies.EnemyName;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpawnerComponent extends Component {
    public static final long WAVE_DELAY = 5000;  // 5 seconds
public static final long SPAWN_DELAY = 3000;  // 3 seconds

    public final GameTime timer;
    public final SpawnerConfig config;
    public long lastTime;
    public int currentWave = 0;
    public boolean isSpawning = false;
    public int enemiesToSpawn = 0;
    public int enemiesSpawned = 0;
    public EnemyName enemyType1 = null;
    public EnemyName enemyType2 = null;
    public EnemyName enemyType3 = null;
    public int enemyType1ToSpawn = 0;
    public int enemyType2ToSpawn = 0;
    public int enemyType3ToSpawn = 0;


    public SpawnerComponent(SpawnerConfig config) {
        this.timer = new GameTime();
        this.lastTime = timer.getTime();
        this.config = config;
    }

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
        LinkedHashMap<String, Integer> currentConfig;
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
        spawnEnemies(currentConfig);
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
        if (enemyType1ToSpawn > 0) {
            spawnEnemy(enemyType1);
            enemyType1ToSpawn--;
            if (entity != null) {
                entity.getEvents().trigger("playSound", "enemySpawn"); // triggering spawning sound effects
            }
        } else if (enemyType2ToSpawn > 0) {
            spawnEnemy(enemyType2);
            enemyType2ToSpawn--;
            if (entity != null) {
                entity.getEvents().trigger("playSound", "enemySpawn"); // triggering spawning sound effects
            }
        } else if (enemyType3ToSpawn > 0) {
            spawnEnemy(enemyType3);
            enemyType3ToSpawn--;
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

    public void spawnEnemies(LinkedHashMap<String, Integer> currentConfig) {
        isSpawning = true;
        for (Map.Entry<String, Integer> entry : currentConfig.entrySet()) {
            if (enemyType1 == null) {
                enemyType1 = EnemyName.getEnemyName(entry.getKey());
                enemyType1ToSpawn = entry.getValue();
            } else if (enemyType2 == null) {
                enemyType2 = EnemyName.getEnemyName(entry.getKey());
                enemyType2ToSpawn = entry.getValue();
            } else if (enemyType3 == null) {
                enemyType3 = EnemyName.getEnemyName(entry.getKey());
                enemyType3ToSpawn = entry.getValue();
            }
        }
        enemiesToSpawn = enemyType1ToSpawn + enemyType2ToSpawn + enemyType3ToSpawn;
    }

    public void spawnEnemy(EnemyName name) {
        Vector2 worldPos = entity.getCenterPosition();
        Entity enemy = EnemyFactory.createEnemy(name);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemy, worldPos);
    }
}
