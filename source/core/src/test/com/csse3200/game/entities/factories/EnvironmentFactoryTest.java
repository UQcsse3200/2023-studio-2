package com.csse3200.game.entities.factories;

import com.badlogic.gdx.maps.MapObject;
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
    public void testCreateEnvironmentEmptyLayer(){
        TiledMapTileLayer emptyLayer = new TiledMapTileLayer(1,1,16,16);
        List<TileEntity> result = EnvironmentFactory.createEnvironment(emptyLayer);
        assertEquals(0, result.size());
    }

    @Test
    public void testCreateEnvironmentWithNoObjects(){
        TiledMapTileLayer layer =mock(TiledMapTileLayer.class);
        when(layer.getHeight()).thenReturn(2);
        when(layer.getWidth()).thenReturn(2);

        List<TileEntity> environments = EnvironmentFactory.createEnvironment(layer);

        // Asserting that the list of environments is empty since there are no objects on the layer
        assertEquals(0, environments.size()); }

//    @Test
//    public void testCreateEnvironmentNonEmptyLayer() {
//        TiledMapTileLayer layer = new TiledMapTileLayer(1, 1, 16, 16);
//        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
//        // Create a RectangleMapObject and set it as the cell's object
//        RectangleMapObject rectangleObject = new RectangleMapObject();
//        cell.getTile().getObjects().add(rectangleObject);
//        layer.setCell(0, 0, cell);
//
//        List<TileEntity> result = EnvironmentFactory.createEnvironment(layer);
//        assertEquals(1, result.size());
//    }


//    @Test
//    public void testCreateEnvironmentMultipleCells() {
//        TiledMapTileLayer layer = new TiledMapTileLayer(2, 2, 16, 16);
//        TiledMapTileLayer.Cell cell1 = new TiledMapTileLayer.Cell();
//        TiledMapTileLayer.Cell cell2 = new TiledMapTileLayer.Cell();
//        RectangleMapObject rectangleObject1 = new RectangleMapObject();
//        RectangleMapObject rectangleObject2 = new RectangleMapObject();
//        cell1.getTile().getObjects().add(rectangleObject1);
//        cell2.getTile().getObjects().add(rectangleObject2);
//        layer.setCell(0, 0, cell1);
//        layer.setCell(1, 1, cell2);
//
//        List<TileEntity> result = EnvironmentFactory.createEnvironment(layer);
//        assertEquals(2, result.size());
//
//    }
}
