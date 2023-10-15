/**
 * The SpawnerComponentTest class is responsible for testing the functionality
 * of the SpawnerComponent class.
 */
package com.csse3200.game.components.npc;

import com.csse3200.game.entities.configs.SpawnerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import com.csse3200.game.entities.factories.EnemyFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

        LinkedHashMap<String, Integer> waveList = new LinkedHashMap<>();
        waveList.put("redGhost", 1);
        waveList.put("necromancer", 1);
        waveList.put("Knight", 1);
        config.wave1 = waveList;
        spawner.handleNewWave(spawner.timer.getTime());
        assertEquals(1, spawner.enemyType1ToSpawn);
        assertEquals(1, spawner.enemyType2ToSpawn);
        assertEquals(1, spawner.enemyType3ToSpawn);
        assertEquals(1, spawner.currentWave);
        assertTrue(spawner.isSpawning);
    }

}
