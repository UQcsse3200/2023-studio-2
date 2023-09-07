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

    @Override
    public void create() {
        super.create();
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

        if (isSpawning && enemiesSpawned < enemiesToSpawn && currentTime - lastTime >= spawnDelay) {
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

        if (enemiesSpawned >= enemiesToSpawn) {
            isSpawning = false;
            enemiesToSpawn = 0;
            enemiesSpawned = 0;
        }
    }
}
