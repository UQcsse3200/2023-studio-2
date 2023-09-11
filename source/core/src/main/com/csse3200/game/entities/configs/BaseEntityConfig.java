package com.csse3200.game.entities.configs;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public String spritePath = "";
    public GridPoint2 position;
    public String[] requiredTextures = null;
}
