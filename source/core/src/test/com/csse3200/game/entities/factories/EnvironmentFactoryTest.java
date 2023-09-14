package com.csse3200.game.entities.factories;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EnvironmentFactoryTest {

    @Test
    public void testCreateEnvironmentWithObstacle() {
        // Create a mock entity to return when createEnvironment is called with an obstacle

        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        StructurePlacementService placementService = mock(StructurePlacementService.class);
        Cell cell = mock(Cell.class);
        TiledMapTile tile = mock(TiledMapTile.class);
        RectangleMapObject rectangleMapObject = mock(RectangleMapObject.class);
        Rectangle rectangle = mock(Rectangle.class);
        MapObjects mapObjects = mock(MapObjects.class);
        mapObjects.add(rectangleMapObject);
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerStructurePlacementService(placementService);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadAll();


        // Configure the mocks
        when(layer.getHeight()).thenReturn(1);
        when(layer.getWidth()).thenReturn(1);
        when(layer.getCell(0, 0)).thenReturn(cell);
        when(cell.getTile()).thenReturn(tile);
        when(tile.getObjects()).thenReturn(mapObjects);
        when(mapObjects.get(0)).thenReturn(rectangleMapObject);
        when(rectangleMapObject.getRectangle()).thenReturn(rectangle);
        rectangle.x = 1f;
        rectangle.y = 1f;
        rectangle.width = 1f;
        rectangle.height = 1f;
        GridPoint2 gridPoint2 = new GridPoint2();
        Entity entity = new Entity();

        // Call the method under test
        EnvironmentFactory.createEnvironment(layer);

        // Verify that PlaceStructureAt is called the correct amount of times
        verify(placementService, times(1)).PlaceStructureAt(
                any(), any(), anyBoolean(), anyBoolean());
    }
}