/**
 * The SpawnerComponentTest class is responsible for testing the functionality
 * of the SpawnerComponent class.
 */
package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.entities.configs.SpawnerConfig;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
//import com.csse3200.game.entities.factories.EnemyFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpawnerComponentTest {

    private SpawnerComponent spawner;
    private SpawnerConfig config;

    @BeforeEach
    public void setup() {
        // Mock dependencies
        config = mock(SpawnerConfig.class);
        spawner = new SpawnerComponent(config);
    }

    @Test
    public void testShouldSpawnNewWave_NotSpawningAndEnoughTimePassed() {
       
        spawner.isSpawning = false;
        spawner.lastTime = spawner.timer.getTime() - SpawnerComponent.WAVE_DELAY;

        boolean result = spawner.shouldSpawnNewWave(spawner.timer.getTime());
        assertTrue(result);
    }

    @Test
    public void testShouldSpawnNewWave_NotEnoughTimePassed() {
    
        spawner.isSpawning = false;
        boolean result = spawner.shouldSpawnNewWave(spawner.timer.getTime());

        assertFalse(result);
    }

    @Test
    public void testShouldSpawnEnemy_IsSpawningAndEnoughTimePassed() {
        spawner.isSpawning = true;
        spawner.enemiesSpawned = 0;
        spawner.enemiesToSpawn = 1;
        spawner.lastTime = spawner.timer.getTime() - SpawnerComponent.SPAWN_DELAY;

        boolean result = spawner.shouldSpawnEnemy(spawner.timer.getTime());
        assertTrue(result);
    }

    @Test
    public void testHandleNewWave_FirstWave() {

        config.wave1 = new int[]{1, 2, 0};
        spawner.handleNewWave(spawner.timer.getTime());
        assertEquals(1, spawner.meleeEnemiesToSpawn);
        assertEquals(2, spawner.rangedEnemiesToSpawn);
        assertEquals(0, spawner.bossEnemiesToSpawn);
        assertEquals(1, spawner.currentWave);
        assertTrue(spawner.isSpawning);
    }

}
