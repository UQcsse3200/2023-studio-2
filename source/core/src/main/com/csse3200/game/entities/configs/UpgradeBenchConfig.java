package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in UpgradeBeanchConfig files
 * to be loaded by the Structure Factory.
 */

public class UpgradeBenchConfig extends BaseEntityConfig {

    public SoundsConfig sounds;
    public UpgradeBenchConfig() {
        this.spritePath = "images/upgradetree/upgradebench.png";
    }
}
