package com.csse3200.game.entities.configs;

public class GateConfig extends BaseEntityConfig {
    public int health = 0;
    public int maxHealth = 0;
    public String openTextureAtlas = "";
    public String closedTextureAtlas = "";

    public GateConfig() {

    }

    public GateConfig(GateConfig config) {
        super(config);

        health = config.health;
        maxHealth = config.maxHealth;
        openTextureAtlas = config.openTextureAtlas;
        closedTextureAtlas = config.closedTextureAtlas;
    }
}
