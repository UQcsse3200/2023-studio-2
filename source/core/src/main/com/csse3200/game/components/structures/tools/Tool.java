package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;

public abstract class Tool {
    public abstract void interact(Entity player, GridPoint2 position);
}
