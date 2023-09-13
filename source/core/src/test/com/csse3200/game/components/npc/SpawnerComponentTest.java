package com.csse3200.game.components.npc;

import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.services.GameTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;


public class SpawnerComponentTest {
    @Mock
    private GameTime gameTime;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSpawnEnemies() {
        // Create a SpawnerComponent with mock dependencies
        ArrayList<Entity> targets = new ArrayList<>();
        SpawnerComponent spawner = new SpawnerComponent(targets);

        // Define test values
        int meleeCount = 5;
        int rangedCount = 3;

        // Mock behavior for gameTime
        when(gameTime.getTime()).thenReturn(0L);

        // Call the spawnEnemies method
        spawner.spawnEnemies(meleeCount, rangedCount);

        // Verify that the component state is updated correctly
        assert spawner.isSpawning();
        assert spawner.getEnemiesToSpawn() == meleeCount + rangedCount;
        assert spawner.getMeleeEnemiesToSpawn() == meleeCount;
        assert spawner.getRangedEnemiesToSpawn() == rangedCount;
        assert spawner.getEnemiesSpawned() == 0;
    }
    @Mock
    private ArrayList<Entity> targets;
    @Test
    public void testUpdate() {
        // Create a SpawnerComponent with mock dependencies
        SpawnerComponent spawner = new SpawnerComponent(targets);

        // Set up the initial state for testing
        spawner.setSpawning(false);
        spawner.setEnemiesSpawned(0);
        spawner.setEnemiesToSpawn(0);
        spawner.setMeleeEnemiesToSpawn(0);
        spawner.setRangedEnemiesToSpawn(0);

        long currentTime = 0L;
        when(gameTime.getTime()).thenReturn(currentTime);

        // Call the update method
        spawner.update();

        // Verify that the state has been updated correctly
        assertFalse(spawner.isSpawning());
        assertEquals(0, spawner.getEnemiesToSpawn());
        assertEquals(0, spawner.getMeleeEnemiesToSpawn());
        assertEquals(0, spawner.getRangedEnemiesToSpawn());
        assertEquals(0, spawner.getEnemiesSpawned());
    }
}


       


