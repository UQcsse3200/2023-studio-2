package com.csse3200.game.areas;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;
@ExtendWith(GameExtension.class)
class EarthGameAreaTest {

    @Test
    void spawnEnvironmentTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        TiledMapTileLayer tileLayer = mock(TiledMapTileLayer.class);
        TiledMap tiledMap = mock(TiledMap.class);
        GdxGame gdxGame = mock(GdxGame.class);
        MapLayers mapLayers = mock(MapLayers.class); // Mock MapLayers

        EarthGameArea earthGameArea = new EarthGameArea(terrainFactory, gdxGame);
        earthGameArea.terrain = mock(TerrainComponent.class);
        when(earthGameArea.terrain.getMap()).thenReturn(tiledMap);
        when(tiledMap.getLayers()).thenReturn(mapLayers); // Return the mock MapLayers
        when(mapLayers.get("Tree Base")).thenReturn(tileLayer); // Return the mock TileLayer

        EarthGameArea spyEarthGameArea = spy(earthGameArea);
        Method spawnEnvironmentMethod = EarthGameArea.class.getDeclaredMethod("spawnEnvironment");
        spawnEnvironmentMethod.setAccessible(true);
        spawnEnvironmentMethod.invoke(spyEarthGameArea);
    }
}
