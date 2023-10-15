package com.csse3200.game.entities.factories;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.csse3200.game.entities.TileEntity;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EnvironmentFactoryTest {

    @Test
    public void testCreateEnvironmentWithNoObjects() {
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        when(layer.getHeight()).thenReturn(2);
        when(layer.getWidth()).thenReturn(2);

        List<TileEntity> environments = EnvironmentFactory.createEnvironment(layer);

        // Asserting that the list of environments is empty since there are no objects on the layer
        assertEquals(0, environments.size());
    }
}
