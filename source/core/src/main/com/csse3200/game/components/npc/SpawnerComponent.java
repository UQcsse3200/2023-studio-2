package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

public class SpawnerComponent extends Component {
    private GameTime timer;
    private long lastTime;
    private ArrayList<Entity> targets;
    private int currentWave;
    private long waveDelay = 20000; // 20 second delay between enemy waves
    private boolean isSpawning;
    private long spawnDelay = 3000; // 3 second delay between enemy spawns
    private int enemiesToSpawn;
    private int enemiesSpawned;

    private int meleeEnemiesToSpawn;

    private int rangedEnemiesToSpawn;

    public SpawnerComponent(ArrayList<Entity> targets) {
        this.timer = new GameTime();
        this.lastTime = timer.getTime();
        this.targets = targets;
    }
    public boolean isSpawning() {
        return isSpawning;
    }

    public void setSpawning(boolean spawning) {
        isSpawning = spawning;
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public void setEnemiesSpawned(int enemiesSpawned) {
        this.enemiesSpawned = enemiesSpawned;
    }

    public int getEnemiesToSpawn() {
        return enemiesToSpawn;
    }

    public void setEnemiesToSpawn(int enemiesToSpawn) {
        this.enemiesToSpawn = enemiesToSpawn;
    }

    public int getMeleeEnemiesToSpawn() {
        return meleeEnemiesToSpawn;
    }

    public void setMeleeEnemiesToSpawn(int meleeEnemiesToSpawn) {
        this.meleeEnemiesToSpawn = meleeEnemiesToSpawn;
    }

    public int getRangedEnemiesToSpawn() {
        return rangedEnemiesToSpawn;
    }

    public void setRangedEnemiesToSpawn(int rangedEnemiesToSpawn) {
        this.rangedEnemiesToSpawn = rangedEnemiesToSpawn;
    }

    @Override
    public void create() {
        super.create();
    }

    private void spawnEnemies(int meleeCount, int rangedCount) {
        isSpawning = true;
        setEnemiesToSpawn(meleeCount + rangedCount);
        enemiesSpawned = 0;
        setMeleeEnemiesToSpawn(meleeCount);
        setRangedEnemiesToSpawn(rangedCount);
    }

    private void spawnEnemy(EnemyType enemyType, EnemyBehaviour behaviour) {
        Vector2 worldPos = entity.getCenterPosition();
        Entity enemy = EnemyFactory.createEnemy(targets, enemyType, behaviour);
        ServiceLocator.getStructurePlacementService().SpawnEntityAtVector(enemy, worldPos);
    }

    @Override
    public void update() {
        super.update();
        long currentTime = timer.getTime();

        if (!isSpawning && currentTime - lastTime >= waveDelay) {
            switch (currentWave) {
                case 0:
                    spawnEnemies(10, 0);
                    break;
                case 1:
                    spawnEnemies(10, 5);
                    break;
                case 2:
                    spawnEnemies(10, 10);
                    break;
            }

            currentWave++;
            lastTime = currentTime;
        }

        if (isSpawning && enemiesSpawned < getEnemiesToSpawn() && currentTime - lastTime >= spawnDelay) {
            if (getMeleeEnemiesToSpawn() > 0) {
                spawnEnemy(EnemyType.Melee, EnemyBehaviour.PTE);
                setMeleeEnemiesToSpawn(getMeleeEnemiesToSpawn() - 1);
            } else if (getRangedEnemiesToSpawn() > 0) {
                spawnEnemy(EnemyType.Ranged, EnemyBehaviour.PTE);
                setRangedEnemiesToSpawn(getRangedEnemiesToSpawn() - 1);
            }
            enemiesSpawned++;
            lastTime = currentTime;
        }

        if (enemiesSpawned >= getEnemiesToSpawn()) {
            isSpawning = false;
            setEnemiesToSpawn(0);
            enemiesSpawned = 0;
        }
    }
}
