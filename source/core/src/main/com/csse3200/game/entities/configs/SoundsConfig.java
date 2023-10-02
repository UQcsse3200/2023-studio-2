package com.csse3200.game.entities.configs;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * This config class is used to load in custom sound files to play using the SoundComponent
 */
public class SoundsConfig {
    /**
     * Used to map sound identification strings to the sound files
     */
    public ObjectMap<String, String> soundsMap = new ObjectMap<>();
}
