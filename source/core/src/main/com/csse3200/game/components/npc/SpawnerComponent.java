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
    private static final long WAVE_DELAY = 1000;  // 20 seconds
    private static final long SPAWN_DELAY = 3000;  // 3 seconds

    private final GameTime timer;
    private final SpawnerConfig config;

    private long lastTime;
    private int currentWave = 0;
    private boolean isSpawning = false;
    private int enemiesToSpawn = 0;
    private int enemiesSpawned = 0;
    private int meleeEnemiesToSpawn = 0;
    private int rangedEnemiesToSpawn = 0;

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

    private boolean shouldSpawnNewWave(long currentTime) {
        return !isSpawning && currentTime - lastTime >= WAVE_DELAY;
    }

    private boolean shouldSpawnEnemy(long currentTime) {
        return isSpawning && enemiesSpawned < enemiesToSpawn && currentTime - lastTime >= SPAWN_DELAY;
    }

    private boolean shouldStopSpawning() {
        return enemiesSpawned >= enemiesToSpawn;
    }

    private void handleNewWave(long currentTime) {
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

    private void handleEnemySpawn(long currentTime) {
        if (meleeEnemiesToSpawn > 0) {
            spawnEnemy(EnemyType.Melee, EnemyBehaviour.PTE);
            meleeEnemiesToSpawn--;
        } else if (rangedEnemiesToSpawn > 0) {
            spawnEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);
            rangedEnemiesToSpawn--;
        }
        enemiesSpawned++;
        lastTime = currentTime;
    }

    private void resetSpawningState() {
        isSpawning = false;
        enemiesToSpawn = 0;
        enemiesSpawned = 0;
    }

    private void spawnEnemies(int meleeCount, int rangedCount) {
        isSpawning = true;
        enemiesToSpawn = meleeCount + rangedCount;
        enemiesSpawned = 0;
        meleeEnemiesToSpawn = meleeCount;
        rangedEnemiesToSpawn = rangedCount;
    }

    private void spawnEnemy(EnemyType enemyType, EnemyBehaviour behaviour) {
        Vector2 worldPos = entity.getCenterPosition();
        Entity enemy = EnemyFactory.createEnemy(enemyType, behaviour);
        ServiceLocator.getStructurePlacementService().spawnEntityAtVector(enemy, worldPos);
    }
}

