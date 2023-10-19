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
    public static final long INITIAL_DELAY = 0;
    public static final long WAVE_DELAY = 30000;  // 30 seconds in between waves
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

        if (currentWave == 0 && startSpawning(currentTime)) {
            handleNewWave(currentTime);
        }

        if (currentWave > 0 && shouldSpawnNewWave(currentTime)) {
            handleNewWave(currentTime);
        }
        if (shouldSpawnEnemy(currentTime)) {
            handleEnemySpawn(currentTime);
        }
        if (shouldStopSpawning()) {
            resetSpawningState();
        }
    }

    public boolean startSpawning(long currentTime) {
        return !isSpawning && currentTime - lastTime >= INITIAL_DELAY;
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
        enemyType1 = null;
        enemyType2 = null;
        enemyType3 = null;
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

    public LinkedHashMap<String, Integer> getWave1() {
        LinkedHashMap<String, Integer> currentWaveContents = new LinkedHashMap<>();

        EnemyName name;
        int count;

        for (Map.Entry<String, Integer> entry : config.wave1.entrySet()) {
            name = EnemyName.getEnemyName(entry.getKey());
            count = entry.getValue();
            if (currentWave == 0) {
                if (enemyType1.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType1ToSpawn);
                }
                if (enemyType2.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType2ToSpawn);
                }
                if (enemyType3.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType3ToSpawn);
                }
            }
            if (currentWave > 0) {
                currentWaveContents.put(name.toString(), 0);
            }
        }
        return currentWaveContents;
    }

    public LinkedHashMap<String, Integer> getWave2() {
        LinkedHashMap<String, Integer> currentWaveContents = new LinkedHashMap<>();

        EnemyName name;
        int count;

        for (Map.Entry<String, Integer> entry : config.wave2.entrySet()) {
            name = EnemyName.getEnemyName(entry.getKey());
            count = entry.getValue();
            if (currentWave == 1) {
                if (enemyType1 == null) {
                    currentWaveContents.put(name.toString(), count);
                } else if (enemyType1.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType1ToSpawn);
                } else {
                    currentWaveContents.put(name.toString(), count);
                }
                if (enemyType2 == null) {
                    currentWaveContents.put(name.toString(), count);
                } else if (enemyType2.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType2ToSpawn);
                } else {
                    currentWaveContents.put(name.toString(), count);
                }
                if (enemyType3 == null) {
                    currentWaveContents.put(name.toString(), count);
                } else if (enemyType3.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType3ToSpawn);
                } else {
                    currentWaveContents.put(name.toString(), count);
                }
            }
            if (currentWave < 1) {
                currentWaveContents.put(name.toString(), count);
            }
            if (currentWave > 1) {
                currentWaveContents.put(name.toString(), 0);
            }
        }
        return currentWaveContents;
    }

    public LinkedHashMap<String, Integer> getWave3() {
        LinkedHashMap<String, Integer> currentWaveContents = new LinkedHashMap<>();

        EnemyName name;
        int count;

        for (Map.Entry<String, Integer> entry : config.wave3.entrySet()) {
            name = EnemyName.getEnemyName(entry.getKey());
            count = entry.getValue();
            if (currentWave == 2) {
                if (enemyType1 == null) {
                    currentWaveContents.put(name.toString(), count);
                } else if (enemyType1.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType1ToSpawn);
                } else {
                    currentWaveContents.put(name.toString(), count);
                }
                if (enemyType2 == null) {
                    currentWaveContents.put(name.toString(), count);
                } else if (enemyType2.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType2ToSpawn);
                } else {
                    currentWaveContents.put(name.toString(), count);
                }
                if (enemyType3 == null) {
                    currentWaveContents.put(name.toString(), count);
                } else if (enemyType3.equals(name)) {
                    currentWaveContents.put(name.toString(), enemyType3ToSpawn);
                } else {
                    currentWaveContents.put(name.toString(), count);
                }
            }
            if (currentWave < 2) {
                currentWaveContents.put(name.toString(), count);
            }
        }
        return currentWaveContents;
    }
}
