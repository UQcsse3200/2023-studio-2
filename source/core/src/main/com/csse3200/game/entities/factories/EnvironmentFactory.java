package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;

public class EnvironmentFactory {

    private static final int tileSize = 16;

    private static final float scaleSize = 0.5f;

    private static final int tileNumber = 60;

    private static final int secretTileNumber= 70;

    public static void createEnvironment(TiledMapTileLayer collisionLayer) {
        StructurePlacementService placementService = ServiceLocator.getStructurePlacementService();
        Entity environment;

        for (int y = 0; y < collisionLayer.getHeight(); y++) {
            for (int x = 0; x < collisionLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, collisionLayer.getHeight() - 1 - y);
                if (cell != null) {
                    MapObjects objects = cell.getTile().getObjects();
                    GridPoint2 tilePosition = new GridPoint2(x, collisionLayer.getHeight() - 1 - y);
                    if (objects.getCount() >= 1) {
                        RectangleMapObject object = (RectangleMapObject) objects.get(0);
                        Rectangle collisionBox = object.getRectangle();
                        float collisionX = scaleSize - collisionBox.x / tileSize;
                        float collisionY = scaleSize - collisionBox.y / tileSize;
                        float collisionWidth = scaleSize * (collisionBox.width / tileSize);
                        float collisionHeight = scaleSize* (collisionBox.height / tileSize);
                        environment = ObstacleFactory.createEnvironment(collisionWidth, collisionHeight, collisionX, collisionY);
                    } else {
                        environment = ObstacleFactory.createEnvironment();
                    }
                    placementService.PlaceStructureAt(environment, tilePosition, false, false);
                }
            }
        }
    }

    public static void createSecretEnvironment(TiledMapTileLayer collisionLayer) {
        StructurePlacementService placementService = ServiceLocator.getStructurePlacementService();
        Entity environment;
        for (int y = secretTileNumber; y < collisionLayer.getHeight(); y++) {
            for (int x = secretTileNumber; x < collisionLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, collisionLayer.getHeight() - 1 - y);
                if (cell != null) {
                    MapObjects objects = cell.getTile().getObjects();
                    GridPoint2 tilePosition = new GridPoint2(x, collisionLayer.getHeight() - 1 - y);
                    if (objects.getCount() >= 1) {
                        RectangleMapObject object = (RectangleMapObject) objects.get(0);
                        Rectangle collisionBox = object.getRectangle();
                        float collisionX = scaleSize - collisionBox.x / tileSize;
                        float collisionY = scaleSize - collisionBox.y / tileSize;
                        float collisionWidth = scaleSize * (collisionBox.width / tileSize);
                        float collisionHeight = scaleSize* (collisionBox.height / tileSize);
                        environment = ObstacleFactory.createEnvironment(collisionWidth, collisionHeight, collisionX, collisionY);
                    } else {
                        environment = ObstacleFactory.createEnvironment();
                    }
                    placementService.PlaceStructureAt(environment, tilePosition, false, false);
                }
            }
        }
    }
}
