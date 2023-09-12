package com.csse3200.game.services;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.terrain.TerrainComponent;

public class TerrainService {
    private final TerrainComponent terrain;
    public TerrainService(TerrainComponent terrain) {
        this.terrain = terrain;
    }
    public Vector2 ScreenCoordsToGameCoords(int screenX, int screenY) {
        var location = terrain.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(location.x, location.y);
    }

}
