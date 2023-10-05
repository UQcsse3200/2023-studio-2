package com.csse3200.game.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.terrain.TerrainComponent;
import org.junit.Before;
import org.junit.Test;

public class TerrainServiceTest {

    private TerrainComponent terrain;

    @Before
    public void setUp() {
        terrain = mock(TerrainComponent.class);
    }

    @Test
    public void testScreenCoordsToGameCoords() {

        TerrainService terrainService = new TerrainService(terrain);
        int screenX = 100;
        int screenY = 200;
        Vector3 mockUnprojectResult = new Vector3(1.0f, 2.0f, 3.0f);


        when(terrain.unproject(new Vector3(screenX, screenY, 0))).thenReturn(mockUnprojectResult);


        Vector2 result = terrainService.ScreenCoordsToGameCoords(screenX, screenY);


        Vector2 expected = new Vector2(1.0f, 2.0f);
        assertEquals(expected, result);
    }
}