package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;

/**
 * Defines the properties stored in walls config files to be loaded by the Wall Factory.
 */
public class WallConfigs {
    public WallConfig basic = new WallConfig();
    public WallConfig intermediate = new WallConfig();
    public WallConfig gate = new WallConfig();

    public WallConfig GetWallConfig(WallType type) {
        return switch (type) {
            case basic -> basic;
            case intermediate -> intermediate;
            case gate -> gate;
        };
    }
}
