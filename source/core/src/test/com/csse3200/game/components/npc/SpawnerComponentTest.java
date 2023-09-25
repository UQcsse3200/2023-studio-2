/**
 * The SpawnerComponentTest class is responsible for testing the functionality
 * of the SpawnerComponent class.
 */
package com.csse3200.game.components.npc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


public class SpawnerComponentTest {
    @Mock
    private GameTime gameTime;

    /**
     * Sets up the dependencies and initializes the mock objects
     * before each test case.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    /**
//     * Tests the spawnEnemies() method of the SpawnerComponent class.
//     */
//    @Test
//    public void testSpawnEnemies() {
//        // Prepare
//        SpawnerComponent spawner = new SpawnerComponent();
//        int meleeCount = 5;
//        int rangedCount = 3;
//
//        // Set up mock behavior
//        when(gameTime.getTime()).thenReturn(0L);
//
//        // Perform the spawning
//        spawner.spawnEnemies(meleeCount, rangedCount);
//
//        // Assert the expected results
//        assertTrue(spawner.isSpawning());
//        assertEquals(meleeCount + rangedCount, spawner.getEnemiesToSpawn());
//        assertEquals(meleeCount, spawner.getMeleeEnemiesToSpawn());
//        assertEquals(rangedCount, spawner.getRangedEnemiesToSpawn());
//        assertEquals(0, spawner.getEnemiesSpawned());
//    }
//
//    @Mock
//    private ArrayList<Entity> targets;
//
//    /**
//     * Tests the update() method of the SpawnerComponent class.
//     */
//    @Test
//    public void testUpdate() {
//        // Prepare
//        SpawnerComponent spawner = new SpawnerComponent(targets);
//        spawner.setSpawning(false);
//        spawner.setEnemiesSpawned(0);
//        spawner.setEnemiesToSpawn(0);
//        spawner.setMeleeEnemiesToSpawn(0);
//        spawner.setRangedEnemiesToSpawn(0);
//        long currentTime = 0L;
//
//        // Set up mock behavior
//        when(gameTime.getTime()).thenReturn(currentTime);
//
//        // Perform the update
//        spawner.update();
//
//        // Assert the expected results of the update method()
//        assertFalse(spawner.isSpawning());
//        assertEquals(0, spawner.getEnemiesToSpawn());
//        assertEquals(0, spawner.getMeleeEnemiesToSpawn());
//        assertEquals(0, spawner.getRangedEnemiesToSpawn());
//        assertEquals(0, spawner.getEnemiesSpawned());
//    }

}

