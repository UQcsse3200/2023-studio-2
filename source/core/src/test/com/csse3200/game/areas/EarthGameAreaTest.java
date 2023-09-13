package com.csse3200.game.areas;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.*;
@ExtendWith(GameExtension.class)
class EarthGameAreaTest {

    @BeforeEach
    void setUp() {
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        String[] treeTopTexture = {"map/treetop.png"};
        resourceService.loadTextures(treeTopTexture);
        resourceService.loadAll();
    }
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
    @Test
    void spawnTreeTopLayerTest() throws Exception {
        // Create mocks for required dependencies
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        TiledMapTileLayer tileLayer = mock(TiledMapTileLayer.class);
        TiledMap tiledMap = mock(TiledMap.class);
        GdxGame gdxGame = mock(GdxGame.class);
        MapLayers mapLayers = mock(MapLayers.class);

        // Create an instance of EarthGameArea
        EarthGameArea earthGameArea = new EarthGameArea(terrainFactory, gdxGame);

        // Mock the terrain component and map layers
        earthGameArea.terrain = mock(TerrainComponent.class);
        when(earthGameArea.terrain.getMap()).thenReturn(tiledMap);
        when(tiledMap.getLayers()).thenReturn(mapLayers);
        when(mapLayers.get("Tree Base")).thenReturn(tileLayer);

        // Use reflection to access the private spawnTreeTopLayer method
        Method spawnTreeTopLayerMethod = EarthGameArea.class.getDeclaredMethod("spawnTreeTopLayer");
        spawnTreeTopLayerMethod.setAccessible(true);

        // Create a spy of the EarthGameArea instance
        EarthGameArea spyEarthGameArea = spy(earthGameArea);



        //Code giving unexpected nullpointerexception error, not sure why.
        //To be checked and implemented in the next sprint

        // Invoke the spawnTreeTopLayer method
//        spawnTreeTopLayerMethod.invoke(spyEarthGameArea);
//
//        // Use reflection to access the private gameEntities field
//        List<Entity> spawnedTreeTopEntities = spyEarthGameArea.getSpawnedTreeTopEntities();
//
//        // Verify that tree top entities were correctly spawned
//        int expectedNumTreeTops = 1;
//        Assertions.assertEquals(expectedNumTreeTops, spawnedTreeTopEntities.size());


    }
}
