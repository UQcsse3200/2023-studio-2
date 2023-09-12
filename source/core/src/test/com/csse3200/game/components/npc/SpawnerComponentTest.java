package com.csse3200.game.components.npc;

import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.services.GameTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

import java.util.ArrayList;

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
      
        ArrayList<Entity> targets = new ArrayList<>();
        SpawnerComponent spawner = new SpawnerComponent(targets);

        int meleeCount = 5;
        int rangedCount = 3;

        when(gameTime.getTime()).thenReturn(0L);

     
        spawner.spawnEnemies(meleeCount, rangedCount);

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
       
        SpawnerComponent spawner = new SpawnerComponent(targets);

  
        spawner.setSpawning(false);
        spawner.setEnemiesSpawned(0);
        spawner.setEnemiesToSpawn(0);
        spawner.setMeleeEnemiesToSpawn(0);
        spawner.setRangedEnemiesToSpawn(0);

        long currentTime = 0L;
        when(gameTime.getTime()).thenReturn(currentTime);

    
        spawner.update();

    
        assertFalse(spawner.isSpawning());
        assertEquals(0, spawner.getEnemiesToSpawn());
        assertEquals(0, spawner.getMeleeEnemiesToSpawn());
        assertEquals(0, spawner.getRangedEnemiesToSpawn());
        assertEquals(0, spawner.getEnemiesSpawned());

       
}
