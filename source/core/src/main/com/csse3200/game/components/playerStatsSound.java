package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * This class is meant to implement
 * player's death sound and its low health sound
 */
public class playerStatsSound {

    private Sound lowHealthSound; // sound object for low health sound effect

    /**
     * @param SoundFile
     * this parameter denotes the Player's Low health audio file
     */
    public playerStatsSound(String SoundFile) {
        lowHealthSound = Gdx.audio.newSound(Gdx.files.internal(SoundFile));
    }

    /**
     * playLowHealthSound plays the sound when player's health is low (below 50)
     */
    public void playLowHealthSound() {
        lowHealthSound.play();
    }

    /**
     *  The dispose method should be called when the sound manager
     *  is no longer needed to release system resources.
     */
    public void dispose() {
        lowHealthSound.dispose();
    }

}
