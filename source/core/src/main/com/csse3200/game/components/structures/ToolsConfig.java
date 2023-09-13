package com.csse3200.game.components.structures;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * This class is used to read in the structure tools config file.
 */
public class ToolsConfig {
    /**
     * A map between a tool class's path and its tool config.
     */
    public ObjectMap<String, ToolConfig> toolConfigs;
}
