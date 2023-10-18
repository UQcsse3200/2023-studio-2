package com.csse3200.game.entities.configs;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.resources.Resource;

public class ExtractorConfig extends BaseEntityConfig {
    public Resource resource;
    public int health;
    public int maxHealth;
    public long tickRate;
    public int tickSize;
    public ParticleEffectsConfig effects;

    public ExtractorConfig() {
        spritePath = "images/structures/ExtractorAnimation.atlas";
    }
}
