package com.csse3200.game.entities.configs;

import com.csse3200.game.components.resources.Resource;

public class ExtractorConfig extends BaseEntityConfig {
    public Resource resource;
    public int health;
    public long tickRate;
    public int tickSize;

    public ExtractorConfig() {
        spritePath = "images/ExtractorAnimation.atlas";
    }
}
