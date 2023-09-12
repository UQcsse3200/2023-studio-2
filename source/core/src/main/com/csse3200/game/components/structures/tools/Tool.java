package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;

public abstract class Tool {
    final ObjectMap<String, Integer> cost;

    protected Tool(ObjectMap<String, Integer> cost) {
        this.cost = cost;
    }

    public abstract boolean interact(Entity player, GridPoint2 position);
}
