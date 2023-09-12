package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;

import java.util.Map;

public abstract class Tool {
    final ObjectMap<String, Integer> cost;

    public Tool(ObjectMap<String, Integer> cost) {
        this.cost = cost;
    }

    public abstract void interact(Entity player, GridPoint2 position);
}
