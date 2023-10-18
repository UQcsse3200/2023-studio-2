package com.csse3200.game.input;

import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class HoleInputComponentTest {

    private HoleInputComponent inputComponent;

    @Test
    public void testTouchDownWithinBoundingBox() {

        TerrainComponent terrain = mock(TerrainComponent.class);
        ExtractorMiniGameArea area = mock(ExtractorMiniGameArea.class);
        Entity entity = mock(Entity.class);
        inputComponent = new HoleInputComponent(terrain, area);
        inputComponent.setEntity(entity);

        // Mock the behavior of the entity
        when(entity.getPosition()).thenReturn(new Vector2(0, 0));
        when(entity.getScale()).thenReturn(new Vector2(2, 2));


        // Call the method under test
        boolean result = inputComponent.touchDown(0, 0, 0, 0);

        // Verify that other methods are not called
        verify(area, never()).spawnExtractorBang(anyInt(), anyInt());
        verify(area, never()).spawnExtractorsHolePart();

        // Assert that the result is true
        assertTrue(result);
    }

    @Test
    public void testTouchDownOutsideBoundingBox() {

        TerrainComponent terrain = mock(TerrainComponent.class);
        ExtractorMiniGameArea area = mock(ExtractorMiniGameArea.class);
        Entity entity = mock(Entity.class);
        inputComponent = new HoleInputComponent(terrain, area);
        inputComponent.setEntity(entity);

        // Mock the behavior of the entity
        when(entity.getPosition()).thenReturn(new Vector2(4, 4));
        when(entity.getScale()).thenReturn(new Vector2(2, 2));

        // Call the method under test
        boolean result = inputComponent.touchDown(0, 0, 0, 0);

        // Verify that the entity is not disposed
        verify(entity, never()).dispose();

        // Verify that other methods are not called
        verify(area, never()).spawnExtractorBang(anyInt(), anyInt());
        verify(area, never()).spawnExtractorsHolePart();

        // Assert that the result is false
        assertFalse(result);
    }

}
