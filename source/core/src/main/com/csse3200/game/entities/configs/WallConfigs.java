package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.buildables.WallType;

/**
 * Defines the properties stored in walls config files
 * to be loaded by the Wall Factory.
 * Also defines sound object to later addition Sound component in factory.
 */
public class WallConfigs {
    public WallConfig basic = new WallConfig();
    public WallConfig intermediate = new WallConfig();
    public WallConfig gate = new WallConfig();
    public SoundsConfig sounds;

    public WallConfig getWallConfig(WallType type) {
        return switch (type) {
            case BASIC -> basic;
            case INTERMEDIATE -> intermediate;
            case GATE -> gate;
        };
    }
}
