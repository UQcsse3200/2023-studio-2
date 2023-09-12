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

}
