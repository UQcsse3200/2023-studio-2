package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * This class is meant to implement
 * player's death sound and its low health sound
 */
public class playerStatsSound {

    private Sound lowHealthSound; // sound object for low health sound effect

    private Sound respawnSound; // sound object for remaining lives for respawn

    private Sound deadSound; //sound object for dead.

    /**
     * @param SoundFile
     * this parameter denotes the Player's Low health audio file and
     * audio file of remaining lives for respawn and
     * audio file for dead when player doesn't have any life to respawn.
     */

    public playerStatsSound(String SoundFile) {
        lowHealthSound = Gdx.audio.newSound(Gdx.files.internal(SoundFile));
        respawnSound = Gdx.audio.newSound(Gdx.files.internal(SoundFile));
        deadSound = Gdx.audio.newSound(Gdx.files.internal(SoundFile));
    }

    /**
     * playLowHealthSound plays the sound when player's health is low (below 50)
     */
    public void playLowHealthSound() {
        lowHealthSound.play();
    }

    /**
     * playRespawnLivesSound plays the sound when the player is dead
     * and still respawn lives are remaining
     */
    public void playRespawnLivesSound() {
        respawnSound.play();
    }

    /**
     * playDeadSound plays the sound when the player is dead
     * and no life is remaining to respawn.
     */
    public void playDeadSound() {
        deadSound.play();
    }

    /**
     *  The dispose method should be called when the sound manager
     *  is no longer needed to release system resources.
     */
    public void dispose() {
        lowHealthSound.dispose();
        respawnSound.dispose();
        deadSound.dispose();
    }

}
