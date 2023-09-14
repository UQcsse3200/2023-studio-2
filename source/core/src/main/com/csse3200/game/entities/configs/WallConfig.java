package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.buildables.WallType;

/**
 * Defines each entries properties stored in wall config file to be loaded by the Wall Factory.
 */
public class WallConfig extends BaseEntityConfig {
    public int health = 0;
    public WallType type;
}
