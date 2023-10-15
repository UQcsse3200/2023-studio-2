package com.csse3200.game.areas;


import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@ExtendWith(GameExtension.class)
public class BrickBreakerGameAreaTest {
    private BrickBreakerGameArea brickBreakerGameArea;
    private TerrainFactory terrainFactory;

    @Before
    public void setUp() {
        terrainFactory = mock(TerrainFactory.class);
        brickBreakerGameArea = new BrickBreakerGameArea(terrainFactory);
    }

    @Test
    public void testEntityPlacement() {
        Entity entity = mock(Entity.class);
        Vector2 expectedPosition = new Vector2(1.0f, 2.0f);

        when(entity.getCenterPosition()).thenReturn(expectedPosition);

        Vector2 actualPosition = entity.getCenterPosition();
        assertEquals(expectedPosition, actualPosition);
    }
}
