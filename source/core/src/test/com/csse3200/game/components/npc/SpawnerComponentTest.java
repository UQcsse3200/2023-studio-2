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


}
