package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in Companion config files to be loaded by the Companion Factory.
 * Extends {@link BaseEntityConfig} to inherit common entity configuration properties.
 */
public class CompanionConfig extends BaseEntityConfig {
    /**
     * The amount of gold associated with the companion.
     */
    public int gold = 1;

    /**
     * The favorite color of the companion.
     */
    public String favouriteColour = "none";

    /**
     * The file path to the bullet texture used by the companion.
     */
    public String bulletTexturePath = "Bullet.png";
}
