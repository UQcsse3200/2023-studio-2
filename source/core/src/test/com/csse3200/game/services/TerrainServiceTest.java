package com.csse3200.game.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.terrain.TerrainComponent;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains unit tests for the {@link TerrainService} class.
 */
public class TerrainServiceTest {

    private TerrainComponent terrain;

    @Before
    public void setUp() {
        terrain = mock(TerrainComponent.class);
    }

    /**
     * Test the {@link TerrainService#ScreenCoordsToGameCoords(int, int)} method.
     * It verifies that screen coordinates are correctly converted to game coordinates.
     */
    @Test
    public void testScreenCoordsToGameCoords() {

        // Create an instance of TerrainService
        TerrainService terrainService = new TerrainService(terrain);

        // Define screen coordinates
        int screenX = 100;
        int screenY = 200;

        // Define the expected result after conversion
        Vector3 mockUnprojectResult = new Vector3(1.0f, 2.0f, 3.0f);

        // Mock the behavior of terrain.unproject to return the expected result
        when(terrain.unproject(new Vector3(screenX, screenY, 0))).thenReturn(mockUnprojectResult);

        // Call the method to convert screen coordinates to game coordinates
        Vector2 result = terrainService.ScreenCoordsToGameCoords(screenX, screenY);

        // Define the expected result
        Vector2 expected = new Vector2(1.0f, 2.0f);

        // Assert that the actual result matches the expected result
        assertEquals(expected, result);
    }
}
